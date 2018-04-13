package me.zen.luna.ioc.test;

import me.zen.luna.ioc.annotation.Bean;
import me.zen.luna.ioc.annotation.Inject;

@Bean
public class B extends A{

    @Inject
    private C c;
}
