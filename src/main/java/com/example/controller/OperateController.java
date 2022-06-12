package com.example.controller;

import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OperateController {

    @Autowired
    private OrderService orderService;


    @GetMapping("create")
    @Transactional
    public Object createOrder() {
        return orderService.create();
    }

    @GetMapping("get")
    public Object getOrder(Long orderId) {
        return orderService.get(orderId);
    }

    @GetMapping("pay")
    public Object pay(Long orderId) {
        return orderService.pay(orderId, UUID.randomUUID().toString());
    }

    @GetMapping("fulfill")
    public Object fulfill(Long orderId) {
        return orderService.fulfill(orderId);
    }
}
