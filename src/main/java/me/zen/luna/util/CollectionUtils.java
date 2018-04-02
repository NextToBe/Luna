package me.zen.luna.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionUtils {

    /**
     *
     * @param collection a collection
     * @return <code>true</code> if the the collection is null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }


    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }


    /**
     *
     * @param <K>  Key type
     * @param <V>  Value type
     * @return return a new HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }


    /**
     *
     * @param <K>  Key type
     * @param <V>  Value type
     * @return return HashMap
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }


}
