package com.framework.service;

import com.framework.spring.annotation.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class BeanPostProcessor implements com.framework.spring.bean.BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if ("userService".equals(beanName)){
            System.out.println("前置置逻辑增强");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if ("userService".equals(beanName)){
            /*Object instance = Proxy.newProxyInstance(BeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("后置逻辑增强");
                    return method.invoke(bean,args);
                }
            });
            return instance;*/
            System.out.println("后置逻辑增强");
        }
        return bean;
    }
}
