package me.zen.luna.ioc;

import me.zen.luna.ioc.config.Configuration;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void testConfig() {
        Configuration configuration = Configuration.from("classpath:application.config");
    }
}
