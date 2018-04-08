package me.zen.luna.ioc;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SimpleContainerImpl implements Container {

    private Map<String, BeanDefination> pool = new HashMap<>(32);


    @Override
    public void add(Object bean) {
        add(bean.getClass().getName(), bean);
    }


    @Override
    public void add(String name, Object bean) {
        BeanDefination beanDefination = new BeanDefination(bean);
        addBean(name, beanDefination);

        // add interfaces to support interface-injection
        Class<?>[] interfaces = beanDefination.getType().getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> interfaceClazz : interfaces) {
                addBean(interfaceClazz.getName(), beanDefination);
            }
        }
    }


    @Override
    public <T> T add(Class<T> type) {
        Object bean = addBean(type, true);
        return type.cast(bean);
    }

    @Override
    public void set(Class<?> type, Object bean) {
        BeanDefination beanDefination = pool.get(type.getName());
        if (beanDefination == null) {
            beanDefination = new BeanDefination(bean, type);
        } else {
            beanDefination.setBean(bean);
        }

        pool.put(type.getName(), beanDefination);
    }

    @Override
    public Object get(String name) {
        BeanDefination beanDefination = pool.get(name);
        if (beanDefination == null) {
            return null;
        }

        return beanDefination.getBean();
    }


    @Override
    public <T> T get(Class<T> type) {
        Object bean = get(type.getName());
        try {
            return type.cast(bean);
        } catch (Exception e) {
            log.error("Error occurred when casting bean :", e);
        }

        return null;
    }


    @Override
    public List<Object> getAll() {
        Set<String> names = pool.keySet();
        List<Object> beans = new ArrayList<>(names.size());
        names.forEach(name -> {
            Object bean = get(name);
            if (bean != null) {
                beans.add(bean);
            }
        });

        return beans;
    }

    @Override
    public <T> boolean remove(Class<T> type) {
        return pool.remove(type.getName()) != null;
    }


    @Override
    public boolean remove(String name) {
        return pool.remove(name) != null;
    }



    @Override
    public boolean clear() {
        pool.clear();
        return true;
    }


    private void addBean(String name, BeanDefination beanDefination) {
        if (pool.put(name, beanDefination) != null) {
            log.warn("Duplicated bean: name = {}", name);
        }
    }

    private Object addBean(Class<?> type, boolean isSingleton) {
        return addBean(type.getName(), type, isSingleton);
    }

    private Object addBean(String name, Class<?> type, boolean isSingleton) {
        BeanDefination beanDefination = createBean(type, isSingleton);
        if (beanDefination == null) {
            return null;
        }

        if (pool.put(name, beanDefination) != null) {
            log.warn("Duplicated Bean: {}", name);
        }

        // add interfaces to support interface-injection
        Class<?>[] interfaces = type.getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> interfaceClazz : interfaces) {
                if (get(interfaceClazz) != null) {
                    break;
                }
                addBean(interfaceClazz.getName(), beanDefination);
            }
        }

        return beanDefination.getBean();
    }



    private BeanDefination createBean(Class<?> type, boolean isSingleton) {
        try {
            Object bean = type.newInstance();
            return new BeanDefination(bean, type, isSingleton);
        } catch (Exception e) {
            log.error("Error occurred when creating bean: ", e);
        }

        return null;
    }
}
