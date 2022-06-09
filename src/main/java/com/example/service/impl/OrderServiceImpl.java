package com.example.service.impl;

import com.example.entity.OrderEntity;
import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.service.OrderService;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private Long id = 1L;
    private Map<Long, OrderEntity> orders = new HashMap<>();

    @Override
    public OrderEntity create() {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.WAIT_PAYMENT);
        order.setId(id++);
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public OrderEntity pay(Long id) {
        OrderEntity order = orders.get(id);
        System.out.println("线程名称：" + Thread.currentThread().getName() + " 尝试支付，订单号：" + id);
        Message message = MessageBuilder.withPayload(OrderEvents.PAYED).
                setHeader("order", order).build();
//        if (!sendEvent(message, order)) {
//            System.out.println("线程名称：" + Thread.currentThread().getName() + " 支付失败, 状态异常，订单号：" + id);
//        }
        return orders.get(id);

    }

    @Override
    public OrderEntity deliver(Long id) {
        OrderEntity order = orders.get(id);
        System.out.println("线程名称：" + Thread.currentThread().getName() + " 尝试发货，订单号：" + id);
//        if (!sendEvent(MessageBuilder.withPayload(OrderEvents.DELIVERY)
//                .setHeader("order", order).build(), orders.get(id))) {
//            System.out.println("线程名称：" + Thread.currentThread().getName() + " 发货失败，状态异常，订单号：" + id);
//        }
        return orders.get(id);
    }

    @Override
    public OrderEntity receive(Long id) {
        OrderEntity order = orders.get(id);
        System.out.println("线程名称：" + Thread.currentThread().getName() + " 尝试收货，订单号：" + id);
//        if (!sendEvent(MessageBuilder.withPayload(OrderEvents.RECEIVED)
//                .setHeader("order", order).build(), orders.get(id))) {
//            System.out.println("线程名称：" + Thread.currentThread().getName() + " 收货失败，状态异常，订单号：" + id);
//        }
        return orders.get(id);
    }

    @Override
    public Map<Long, OrderEntity> getOrders() {
        return orders;
    }


}
