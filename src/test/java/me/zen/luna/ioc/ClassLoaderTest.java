package me.zen.luna.ioc;

import me.zen.luna.ioc.annotation.Bean;
import me.zen.luna.ioc.loader.ClassLoaderImpl;
import me.zen.luna.ioc.loader.Scanner;
import me.zen.luna.ioc.test.A;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ClassLoaderTest {

    @Test
    public void testLoadJar() {
        String packageName = "lombok.extern.slf4j";
        ClassLoaderImpl classLoader = new ClassLoaderImpl();
        Set<Class<?>> result = classLoader.load(Scanner.builder().packageName(packageName).build());
        Assert.assertNotNull(result);
    }


    @Test
    public void testLoadPackage() {
        String packageName = "me.zen.luna.ioc.test";
        ClassLoaderImpl classLoader = new ClassLoaderImpl();

        Set<Class<?>> result = classLoader.load(Scanner.builder().packageName(packageName).build());
        Assert.assertEquals(result.size(), 3);

        result = classLoader.load(Scanner.builder().packageName(packageName).superClass(A.class).build());
        Assert.assertEquals(result.size(), 1);

        result = classLoader.load(Scanner.builder().packageName(packageName).annotation(Bean.class).build());
        Assert.assertEquals(result.size(), 1);


    }
}
