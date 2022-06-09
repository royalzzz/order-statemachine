package com.example.service;

import com.example.entity.OrderEntity;

import java.util.Map;

public interface OrderService {

    //创建新订单
    OrderEntity create();
    //发起支付
    OrderEntity pay(Long id);
    //订单发货
    OrderEntity deliver(Long id);
    //订单收货
    OrderEntity receive(Long id);
    //获取所有订单信息
    Map<Long, OrderEntity> getOrders();

}
