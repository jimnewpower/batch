package com.example.easy;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataHandler {

    public void translateCSVToSQLInsertText(String resourceName, String tableName, String columns) {
        Resource resource = new ClassPathResource(resourceName);
        try (
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("UUID") || line.startsWith("userId")) {
                    continue;
                }
                System.out.print("INSERT INTO " + tableName + " " + columns + " VALUES (");
                String[] fields = line.split(",");
                for (int i = 0; i < fields.length; i++) {
                    String field = fields[i];
                    if (field.contains("'")) {
                        field = field.replace("'", "''");
                    }
                    System.out.print("'" + field + "'");
                    if (i < fields.length - 1) {
                        System.out.print(",");
                    }
                }
                System.out.println(");");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
