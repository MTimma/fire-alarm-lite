import express from 'express';
import cors from 'cors';
import fs from 'fs';
import path from 'path';
import { v4 as uuid } from 'uuid';
import { format } from 'date-fns';
import { utcToZonedTime } from 'date-fns-tz';

/**
 * Simple message interface matching React frontend expectations
 */
interface AlarmMessage {
  id: string;
  fireDepartmentCalled: boolean;
  apartment: number;
  comment: string;
  sentAt: string;
}

const app = express();
const PORT = Number(process.env.PORT) || 8888;
// Default to a writable local folder in dev; allow override via DATA_DIR env
const DATA_DIR = process.env.DATA_DIR || path.join(process.cwd(), 'data');
const MSG_FILE = path.join(DATA_DIR, 'messages.json');
const TIMEZONE = process.env.TIMEZONE || 'Europe/Riga';
const ALLOWED_ORIGINS = process.env.ALLOWED_ORIGINS 
  ? process.env.ALLOWED_ORIGINS.split(',').map(origin => origin.trim())
  : ['http://localhost:5173', 'http://localhost:3000'];

// Configure CORS with specific allowed origins for security
app.use(cors({
  origin: (origin, callback) => {
    // Allow requests with no origin (like mobile apps or curl)
    if (!origin) return callback(null, true);
    
    if (ALLOWED_ORIGINS.includes(origin)) {
      callback(null, true);
    } else {
      callback(new Error('Not allowed by CORS'));
    }
  },
  credentials: true
}));
app.use(express.json());

/** Ensure data directory & initial file exist */
function ensureStorage() {
  if (!fs.existsSync(DATA_DIR)) {
    fs.mkdirSync(DATA_DIR, { recursive: true });
  }
  if (!fs.existsSync(MSG_FILE)) {
    fs.writeFileSync(MSG_FILE, JSON.stringify([]));
  }
}

function readMessages(): AlarmMessage[] {
  ensureStorage();
  const raw = fs.readFileSync(MSG_FILE, 'utf-8') || '[]';
  return JSON.parse(raw);
}

function writeMessages(list: AlarmMessage[]) {
  ensureStorage();
  fs.writeFileSync(MSG_FILE, JSON.stringify(list));
}

app.get('/api/messages', (_, res) => {
  try {
    const messages = readMessages();
    res.json(messages);
  } catch (err) {
    console.error(err);
    res.status(503).send('Service Unavailable');
  }
});

app.post('/api/submit', (req, res) => {
  const { isFireDepartmentCalled, apartment, comment } = req.body as {
    isFireDepartmentCalled: boolean;
    apartment: number;
    comment: string;
  };

  if (
    typeof apartment !== 'number' ||
    apartment < 1 ||
    typeof comment !== 'string' ||
    !comment.trim()
  ) {
    return res.status(400).send('Invalid message payload');
  }

  // Format date in the specified timezone (Europe/Riga by default)
  const now = new Date();
  const zonedDate = utcToZonedTime(now, TIMEZONE);
  const formattedDate = format(zonedDate, 'dd.MM.yyyy HH:mm');

  const newMsg: AlarmMessage = {
    id: uuid(),
    fireDepartmentCalled: Boolean(isFireDepartmentCalled),
    apartment,
    comment: comment.trim(),
    sentAt: formattedDate,
  };

  try {
    const messages = readMessages();
    messages.unshift(newMsg);
    writeMessages(messages);
    res.json({ ok: true });
  } catch (err) {
    console.error(err);
    res.status(503).send('Service Unavailable');
  }
});

app.listen(PORT, () => {
  console.log(`FireAlarm backend listening on port ${PORT}`);
});

