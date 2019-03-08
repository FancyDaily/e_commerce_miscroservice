package com.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;

@SpringBootApplication(exclude = XADataSourceAutoConfiguration.class)
class Main {


    static void main(String[] args) {


        SpringApplication.run(Main.class, args);


    }
}
