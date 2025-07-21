package com.itrust.middlewares.nbc.services;


import com.itrust.middlewares.nbc.DateTimeUtil;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Service
public class PropertiesService {

    public void updateToken(String token) throws IOException {
        Properties properties = new Properties();

        // Load the existing properties
        String propertiesFilePath = "src/main/resources/application.properties";
        try (InputStream inputStream = Files.newInputStream(Paths.get(propertiesFilePath))) {
            properties.load(inputStream);
        }

        // Update or add the new property
        properties.setProperty("nbc.token", token);
        properties.setProperty("token", token);

        // Save the updated properties back to the file
        try (FileOutputStream outputStream = new FileOutputStream(propertiesFilePath)) {
            properties.store(outputStream, "Updated by at "+ DateTimeUtil.dateTime());
        }

    }

}

