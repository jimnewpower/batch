package com.example.homegrown;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

class ResourceReaderText implements ResourceReader<String> {

    private String resourceName;

    ResourceReaderText(String resourceName) {
        this.resourceName = Objects.requireNonNull(resourceName, "resource name");
    }

    @Override
    public Collection<String> read() {
        List<String> lines = new ArrayList<>();
        Resource resource = new ClassPathResource(resourceName);
        try (
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
