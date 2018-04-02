package me.zen.luna.util;

public class ReflectUtils {

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz == boolean.class || clazz == Boolean.class || clazz == double.class
            || clazz == Double.class || clazz == float.class || clazz == Float.class
            || clazz == short.class || clazz == Short.class || clazz == int.class || clazz == Integer.class
            || clazz == long.class || clazz == Long.class || clazz == String.class || clazz == byte.class
            || clazz == Byte.class || clazz == char.class || clazz == Character.class;
    }

    public static boolean isPrimitive(Object object) {
        return object instanceof Boolean || object instanceof Double || object instanceof Float
            || object instanceof Short || object instanceof Integer || object instanceof Long
            || object instanceof String || object instanceof Byte || object instanceof Character;
    }
}
