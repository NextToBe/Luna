package me.zen.luna.util;

public class Assert {

    public static void notNull(Object object, String msg) {
        if (null == object) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notEmpty(String str, String msg) {
        if (null == str || "".equals(str)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T> void notEmpty(T[] arr, String msg) {
        if (null == arr || arr.length == 0) {
            throw new IllegalArgumentException(msg);
        }
    }
}
