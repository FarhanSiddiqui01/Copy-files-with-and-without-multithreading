package com.countcharacters.countcharacters.domain;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CountDownLatch;

public class FileProcessor implements Runnable {

    private final String filePath;

    private final CountDownLatch latch;

    public FileProcessor(String filePath, CountDownLatch latch) {
        this.filePath = filePath;
        this.latch = latch;
    }

    public void run() {
        String threadName = Thread.currentThread().getName();
        int charCount = 0;
        try {
            // Convert the file path to a Path
            Path originalFilePath = Path.of(filePath);
            Path tempFilePath = Files.copy(originalFilePath, Path.of("temp", originalFilePath.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            String content = Files.readString(tempFilePath);
            charCount = content.length();
            System.out.println("Thread: " + threadName + ", Filename: " + tempFilePath + ", Char Count: " + charCount);
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + filePath, e);
        } finally {
            latch.countDown();
        }
    }
}
