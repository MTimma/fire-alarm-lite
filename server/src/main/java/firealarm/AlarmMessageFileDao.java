package firealarm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.util.LinkedList;

@Component
public class AlarmMessageFileDao {
    private final String messagesFilePath;

    public AlarmMessageFileDao(@Value("${messages.file}") String messagesFilePath) throws IOException
    {
        Assert.notNull(messagesFilePath,"Property 'messages.file' is not set");
        this.messagesFilePath = messagesFilePath;
        File messsagesFile = new File(messagesFilePath);
        boolean newCreated = messsagesFile.createNewFile();
        if (newCreated) {
            this.initEmpty(messsagesFile);
        }
    }

    public synchronized LinkedList<AlarmMessageDTO> getAllMessages() throws IOException, ClassNotFoundException
    {
        ObjectInputStream oos = null;
        try {
            FileInputStream fos = new FileInputStream(messagesFilePath);
            oos = new ObjectInputStream(fos);
            return (LinkedList<AlarmMessageDTO>)oos.readObject();
        } finally {
            if (oos!=null) {
                oos.close();
            }
        }
    }

    public synchronized void save(AlarmMessageDTO alarmMessageDTO) throws IOException, ClassNotFoundException
    {
        LinkedList<AlarmMessageDTO> messages = this.getAllMessages();
        messages.push(alarmMessageDTO);
        FileOutputStream fos = new FileOutputStream(messagesFilePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(messages);
        oos.close();
    }

    public synchronized void initEmpty(File messsagesFile) throws IOException
    {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(messsagesFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(new LinkedList<>());
            oos.close();
        } finally {
            if (oos!=null) {
                oos.close();
            }
        }

    }
}
