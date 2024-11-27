package com.framework.service;

import com.framework.spring.MiniSpringApplicationContext;

public class Test {
    public static void main(String[] args) {
        MiniSpringApplicationContext context = new MiniSpringApplicationContext(AppConfig.class);

        UserService userService =(UserService) context.getBean("userService");
        System.out.println(userService);
        System.out.println(userService);
        System.out.println(userService);
        System.out.println(userService);
    }
}
