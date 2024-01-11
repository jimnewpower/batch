package com.example.homegrown;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

class TempFileCreator {

    Path createTempFileFromResource(String resourceName) throws IOException {
        Path tempFile = Files.createTempFile("resource-", ".tmp");
        tempFile.toFile().deleteOnExit();

        Resource resource = new ClassPathResource(resourceName);
        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }
}
