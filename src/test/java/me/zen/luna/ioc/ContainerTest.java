package me.zen.luna.ioc;

import me.zen.luna.ioc.test.C;
import org.junit.Test;

public class ContainerTest {

    @Test
    public void testContainer() {
        Container container = new SimpleContainerImpl();
        //container.scanPackages("me.zen.luna.ioc.test");
        container.init();

        container.get(C.class);

    }
}