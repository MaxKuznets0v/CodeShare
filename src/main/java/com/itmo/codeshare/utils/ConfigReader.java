package com.itmo.codeshare.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String configFile = "config.properties";
    private final Properties properties = new Properties();

    public ConfigReader() throws IOException {
        InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream(configFile);
        properties.load(stream);
    }
    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
