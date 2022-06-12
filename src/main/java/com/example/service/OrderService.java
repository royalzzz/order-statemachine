package com.example.service;

import com.example.entity.OrderEntity;

import java.util.Map;

public interface OrderService {

    OrderEntity get(Long orderId);
    //创建新订单
    OrderEntity create();
    //发起支付
    OrderEntity pay(Long orderId, String paymentConfirmationNumber);
    //订单完成
    OrderEntity fulfill(Long orderId);

}
