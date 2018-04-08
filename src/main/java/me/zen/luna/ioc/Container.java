package me.zen.luna.ioc;

import java.util.List;

public interface Container {

    /**
     * add bean into container
     * @param bean the bean instance
     */
    void add(Object bean);


    /**
     * ad bean into container
     * @param name the bean name
     * @param bean the bean instance
     */
    void add(String name, Object bean);


    /**
     * update the bean
     * @param type the bean's type
     * @param bean the bean
     */
    void set(Class<?> type, Object bean);


    /**
     *
     * @param type the bean class type
     * @param <T> class
     * @return constructor
     */
    <T> T add(Class<T> type);


    /**
     * get bean from the contaniner
     * @param name the bean name
     * @return the bean instance
     */
    Object get(String name);


    /**
     *
     * @param type the bean class type
     * @param <T> class
     * @return bean instance
     */
    <T> T get(Class<T> type);


    /**
     * @return all beans
     */
    List<Object> getAll();


    /**
     * remove the bean
     * @param name the bean name
     * @return <code>true</code>> if successfully removed
     */
    boolean remove(String name);


    /**
     *
     * @param type the bean class type
     * @param <T> class
     * @return <code>true</code>> if successfully removed
     */
    <T> boolean remove(Class<T> type);


    /**
     * clear the container
     * @return true if successfully clean
     */
    boolean clear();


}
