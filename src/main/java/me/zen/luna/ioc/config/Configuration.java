package me.zen.luna.ioc.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zen.luna.util.StringUtils;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Configuration {

    // Classpath prefix
    private static final String PREFIX_CLASSPATH = "classpath:";

    // File prefix
    private static final String PREFIX_FILE = "file:";

    private static final String PREFIX_URL = "url:";

    // Configurations
    private Properties config = new Properties();

    public static Configuration newInstance() {
        return new Configuration();
    }


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

    public static Configuration from(URL url) {
        try {
            return from(url.openStream());
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }

    public static Configuration from(String location) {
        if (location.startsWith(PREFIX_CLASSPATH)) {
            location = location.substring(PREFIX_CLASSPATH.length());
            return loadClasspath(location);
        } else if (location.startsWith(PREFIX_FILE)) {
            location = location.substring(PREFIX_FILE.length());
            return from(new File(location));
        } else if (location.startsWith(PREFIX_URL)) {
            location = location.substring(PREFIX_URL.length());
            try {
                return from(new URL(location));
            } catch (MalformedURLException e) {
                log.error("", e);
                return null;
            }
        } else {
            return loadClasspath(location);
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


    private static Configuration loadClasspath(String classpath) {
        InputStream is = getClassLoader().getResourceAsStream(classpath);
        if (null == is) {
            return new Configuration();
        }
        return from(is);
    }

    public static ClassLoader getClassLoader() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception ignored) {
        }
        if (loader == null) {
            loader = Configuration.class.getClassLoader();
            if (loader == null) {
                try {
                    // getClassLoader() returning null indicates the bootstrap ClassLoader
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return loader;
    }


}
