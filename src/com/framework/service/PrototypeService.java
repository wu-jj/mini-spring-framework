package com.framework.service;

import com.framework.spring.annotation.Autowired;
import com.framework.spring.annotation.Component;
import com.framework.spring.annotation.Scope;

@Component
@Scope
public class PrototypeService {
    public String field;
    public PrototypeService(){
        this.field = "PrototypeService";
    }

    @Autowired
    UserService userService;

    public void test(){
        System.out.println(userService.field);
    }
}
