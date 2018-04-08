package me.zen.luna.ioc.injector;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zen.luna.ioc.Container;
import me.zen.luna.ioc.annotation.Inject;

/**
 * inject a bean if a field annotated with {@link Inject}
 */
@Slf4j
@AllArgsConstructor
public class FieldInjector implements Injector{

    private Container container;

    private Field field;

    public void inject(Object bean) {
        try {
            Class<?> type = field.getType();
            Object value = container.get(type);

            if (value == null) {
                throw new IllegalStateException("Cannot inject bean:" + type.getName() + "  for field:" + field.getName());
            }
            field.setAccessible(true);
            field.set(bean, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
