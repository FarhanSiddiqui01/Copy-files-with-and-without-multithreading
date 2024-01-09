package com.countcharacters.countcharacters.controller;


import com.countcharacters.countcharacters.domain.FileProcessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/file")
public class FileCharacterCountController {

    private static final String DEFAULT_FILE_DIRECTORY = "files";

    @PostMapping("/copyFileAndCount-characters")
    public void processFiles(@RequestBody List<String> relativeFilePaths) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(relativeFilePaths.size());
        for (String filename : relativeFilePaths) {
            String filePath = Path.of(DEFAULT_FILE_DIRECTORY, filename).toString();
            FileProcessor fileProcessor = new FileProcessor(filePath, latch);
            new Thread(fileProcessor).start();
        }
        latch.await();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("millisecond took:" + totalTime);
    }

    @PostMapping("/copyFileAndCount-charactersSimple")
    public void processFilesWithoutMultiThreading(@RequestBody List<String> relativeFilePaths) {
        long startTime = System.currentTimeMillis();
        for (String filename : relativeFilePaths) {
            int charCount = 0;
            try {
                String filePath = Path.of(DEFAULT_FILE_DIRECTORY, filename).toString();
                Path originalFilePath = Path.of(filePath);
                Path tempFilePath = Files.copy(originalFilePath, Path.of("temp", originalFilePath.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                String content = Files.readString(tempFilePath);
                charCount = content.length();
                System.out.println("Filename: " + tempFilePath + ", Char Count: " + charCount);
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + filename, e);
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("millisecond took:" + totalTime);
    }
}
