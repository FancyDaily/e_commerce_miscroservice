package com.miscroservice.module.module2;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Test2 {


    @PostConstruct
    public void init() {
        System.out.println("______init____");
    }


}
