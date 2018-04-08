package me.zen.luna.ioc.loader;

import java.util.Set;

public interface ClassLoader {
    Set<Class<?>> load(Scanner scanner);
}
