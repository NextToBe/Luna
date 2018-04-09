package me.zen.luna.ioc;

import java.util.concurrent.ConcurrentHashMap;

public class ClassDefination {

    private static ConcurrentHashMap<Class<?>, ClassDefination> pool = new ConcurrentHashMap<>(128);

    private final Class<?> clazz;


    private ClassDefination(Class<?> clazz) {
        this.clazz = clazz;
    }


    public static ClassDefination put(Class<?> clazz) {
        ClassDefination classDefine = pool.get(clazz);
        if (classDefine == null) {
            classDefine = new ClassDefination(clazz);
            ClassDefination old = pool.putIfAbsent(clazz, classDefine);
            if (old != null) {
                classDefine = old;
            }
        }
        return classDefine;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}