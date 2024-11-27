package com.framework.service;

import com.framework.spring.context.MiniSpringApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        MiniSpringApplicationContext context = new MiniSpringApplicationContext(AppConfig.class);

        UserService userService =(UserService) context.getBean("userService");
        OrderService orderService =(OrderService) context.getBean("orderService");
        PrototypeService prototypeService =(PrototypeService) context.getBean("prototypeService");
        userService.test();
        orderService.test();
        prototypeService.test();
    }
}
