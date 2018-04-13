package me.zen.luna.ioc;

import lombok.extern.slf4j.Slf4j;
import me.zen.luna.ioc.annotation.Bean;
import me.zen.luna.ioc.annotation.Inject;
import me.zen.luna.ioc.injector.FieldInjector;
import me.zen.luna.ioc.loader.ClassLoader;
import me.zen.luna.ioc.loader.ClassLoaderImpl;
import me.zen.luna.ioc.loader.Scanner;
import me.zen.luna.util.Constants;
import me.zen.luna.util.ReflectUtils;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class SimpleContainerImpl implements Container {

    private Map<String, BeanDefination> pool = new HashMap<>(32);

    private Set<String> packages = new LinkedHashSet<>(Constants.basePackages);

    @Override
    public void scanPackages(String... packageName) {
        packages.addAll(Arrays.asList(packageName));
    }

    @Override
    public Set<String> scanPackages() {
        return packages;
    }

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

    @Override
    public void init() {
        if (!pool.isEmpty()) {
            return;
        }

        this.packages.stream()
        .flatMap(this::loadClasses)
        .filter(ReflectUtils::isNormalClass)
        .forEach(clazz -> {
            // register bean
            if (clazz.isAnnotationPresent(Bean.class)) {
                add(clazz);
            }
        });

        if (!pool.isEmpty()) {
            pool.values().forEach(this::inject);
        }

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

    private Stream<Class<?>> loadClasses(String packageName) {
        ClassLoader classLoader = new ClassLoaderImpl();
        Scanner        scanner    = Scanner.builder().packageName(packageName).recursive(true).build();
        Set<Class<?>> classInfos = classLoader.load(scanner);
        return classInfos.stream();
    }

    private void inject(BeanDefination beanDefination) {
        ClassDefination.put(beanDefination.getType());
        List<FieldInjector> fieldInjectors = new ArrayList<>();
        ReflectUtils.getAllFieldsListWithAnnoation(beanDefination.getType(), Inject.class)
        .forEach(field -> fieldInjectors.add(new FieldInjector(this, field)));

        fieldInjectors.forEach(injector -> injector.inject(beanDefination.getBean()));
    }
}
