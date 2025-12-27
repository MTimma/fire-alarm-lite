# Use Node.js 18 LTS as base image
FROM node:18-alpine

# Set the working directory in the container
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy TypeScript source files
COPY tsconfig.json ./
COPY src ./src

# Build the application
RUN npm run build

# Create data directory
RUN mkdir -p /app/data

# Expose the port that your application listens on
EXPOSE 8888

# Set environment variables
ENV NODE_ENV=production
ENV PORT=8888
ENV DATA_DIR=/app/data
ENV TIMEZONE=Europe/Riga

# Run the application
CMD ["node", "dist/index.js"]
