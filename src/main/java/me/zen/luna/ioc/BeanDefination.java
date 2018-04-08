package me.zen.luna.ioc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeanDefination {

    private Object bean;

    private Class<?> type;

    private boolean isSingleton;


    public BeanDefination(Object bean) {
        this.bean = bean;
    }

    public BeanDefination(Object bean, Class<?> type) {
        this.bean = bean;
        this.type = type;
    }

}
