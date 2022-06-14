package com.example.service;

import com.example.entity.OrderEntity;

public interface OrderService {

    OrderEntity get(Long orderId);

    OrderEntity create();

    OrderEntity pay(Long orderId, String payCode);

    OrderEntity fulfill(Long orderId);

    OrderEntity cancel(Long orderId);
}
