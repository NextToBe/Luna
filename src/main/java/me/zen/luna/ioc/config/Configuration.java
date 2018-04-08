package me.zen.luna.ioc.config;

import lombok.extern.slf4j.Slf4j;
import me.zen.luna.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Configuration {

    // Classpath prefix
    private static final String PREFIX_CLASSPATH = "classpath:";

    // File prefix
    private static final String PREFIX_FILE = "file:";

    // Configurations
    private Properties config = new Properties();


    public static Configuration from(Properties properties) {
        Configuration configuration = new Configuration();
        configuration.config = properties;
        return configuration;
    }


    public static Configuration from(Map<String, String> map) {
        Configuration configuration = new Configuration();
        map.forEach((k, v) -> configuration.config.setProperty(k, v));
        return configuration;
    }


    public static Configuration from(File file) {
        try {
            return from(Files.newInputStream(Paths.get(file.getPath())));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Configuration from (InputStream is) {
        try {
            Configuration configuration = new Configuration();
            configuration.config.load(new InputStreamReader(is, "UTF-8"));
            return configuration;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("Close inputstream error:", e);
            }
        }
    }


    public Configuration add(String key, Object value) {
        this.config.setProperty(key, value.toString());
        return this;
    }


    public Configuration addAll(Map<String, Object> map) {
        map.forEach((k, v) -> this.config.setProperty(k, v.toString()));
        return this;
    }

    public String get(String key) {
        if (StringUtils.isEmpty(key)) {
            return StringUtils.EMPTY_STRING;
        }

        return this.config.getProperty(key);
    }

    public Integer getInteger(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        String value  = this.config.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Integer.parseInt(value);

    }

    public Long getLong(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        String value  = this.config.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Long.parseLong(value);
    }

    public Boolean getBoolean(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        String value  = this.config.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Boolean.parseBoolean(value);
    }


    public boolean hasKey(String key) {
        return !StringUtils.isEmpty(key) && this.config.containsKey(key);
    }

    public boolean hasValue(String value) {
        return this.config.containsValue(value);
    }

    public int size() {
        return config.size();
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>(config.size());
        config.forEach((k, v) -> map.put(k.toString(), v.toString()));
        return map;
    }

}
