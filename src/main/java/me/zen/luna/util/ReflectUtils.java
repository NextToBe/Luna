package me.zen.luna.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public final class ReflectUtils {

    private static ConcurrentMap<Class<?>, List<Field>> fieldsCache = new ConcurrentHashMap<>();

    private static ConcurrentMap<Class<? extends Annotation>, ConcurrentMap<Class<?>, List<Field>>> annotatedFieldsCache = new ConcurrentHashMap<>();


    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     *
     * @param clazz the class
     * @return all fields of <code>clazz</code>, including pulibc, private, protected and superclasses' fields
     */
    public static List<Field> getAllFields(final Class<?> clazz) {
        Assert.notNull(clazz, "The class must not be null");
        if (fieldsCache.containsKey(clazz)) {
            return fieldsCache.get(clazz);
        }

        final List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            currentClass = currentClass.getSuperclass();
        }
        fieldsCache.put(clazz, fields);
        return fields;
    }

    /**
     *
     * @param clazz the class
     * @param annotation the annotation
     * @return all fields of <code>clazz</code> annotated with <code>annotation</code>
     */
    public static List<Field> getAllFieldsListWithAnnoation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (!annotatedFieldsCache.containsKey(annotation)) {
            annotatedFieldsCache.putIfAbsent(annotation, new ConcurrentHashMap<>());
        }

        ConcurrentMap<Class<?>, List<Field>> cache = annotatedFieldsCache.get(annotation);
        if (cache.containsKey(clazz)) {
            return cache.get(clazz);
        }

        List<Field> fields = getAllFields(clazz);
        final List<Field> annotatedFields = fields.stream().filter(field -> field.isAnnotationPresent(annotation)).collect(Collectors.toList());

        cache.put(clazz, annotatedFields);
        return annotatedFields;
    }




}
