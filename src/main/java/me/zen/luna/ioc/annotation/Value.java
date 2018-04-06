package me.zen.luna.ioc.annotation;

import java.lang.annotation.*;


@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {

  String name() default "";
}
