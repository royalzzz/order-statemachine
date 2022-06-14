package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.entity"})
public class OrderStateMachineApp {

    public static void main(String[] args) {
        SpringApplication.run(OrderStateMachineApp.class, args);
    }
}
