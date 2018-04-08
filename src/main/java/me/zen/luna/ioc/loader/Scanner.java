package me.zen.luna.ioc.loader;

import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;

@Data
@Builder
public class Scanner {

    private String packageName;

    private boolean recursive;

    private Class<?> superClass;

    private Class<? extends Annotation> annotation;
}
