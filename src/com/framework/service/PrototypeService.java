package com.framework.service;

import com.framework.spring.annotation.Autowired;
import com.framework.spring.annotation.Component;

@Component
public class OrderService {
    public String field;
    public OrderService(){
        this.field = "OrderService";
    }

    @Autowired
    UserService userService;

    public void test(){
        System.out.println(userService.field);
    }
}
