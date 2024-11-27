package com.framework.service;

import com.framework.spring.annotation.Autowired;
import com.framework.spring.annotation.Component;


@Component
public class UserService{
    public String field;
    public UserService(){
        this.field = "UserService";
    }

    @Autowired
    OrderService orderService;


    public void test(){
        System.out.println(orderService.field);
    }



}
