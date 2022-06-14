package com.example.service.impl;

import com.example.entity.OrderEntity;
import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.repo.OrderRepo;
import com.example.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    StateMachine<OrderStatus, OrderEvents> currentStateMachine;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private StateMachineService<OrderStatus, OrderEvents> stateMachineService;

    @Autowired
    private StateMachineFactory<OrderStatus, OrderEvents> stateMachineFactory;

    @Autowired
    private StateMachineRuntimePersister<OrderStatus, OrderEvents, String> stateMachineRuntimePersister;

    @Override
    public OrderEntity get(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }

    @Override
    public OrderEntity create() {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.SUBMITTED);
        order.setCreateTime(new Date().toInstant());
        orderRepo.saveAndFlush(order);
        StateMachine<OrderStatus, OrderEvents> stateMachine = this.getStateMachine(order.getId());
        logger.info("start state machine: " + stateMachine.getId() + "\tuuid: " + stateMachine.getUuid());
        return order;
    }

    @Override
    public OrderEntity fulfill(Long orderId) {
        StateMachine<OrderStatus, OrderEvents> stateMachine = this.getStateMachine(orderId);
        logger.info("before calling fulfill(): " + stateMachine.getState().getId());
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.FULFILL)
                .setHeader("orderId", orderId)
                .build())).blockLast();
        logger.info("after calling fulfill(): " + stateMachine.getState().getId());
        return get(orderId);
    }

    @Override
    public OrderEntity cancel(Long orderId) {
        StateMachine<OrderStatus, OrderEvents> stateMachine = this.getStateMachine(orderId);
        logger.info("before calling cancel(): " + stateMachine.getState().getId());
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.CANCEL)
                .setHeader("orderId", orderId)
                .build())).blockLast();
        logger.info("after calling cancel(): " + stateMachine.getState().getId());
        return get(orderId);
    }

    @Override
    public OrderEntity pay(Long orderId, String payCode) {
        StateMachine<OrderStatus, OrderEvents> stateMachine = this.getStateMachine(orderId);
        logger.info("before calling pay(): " + stateMachine.getState().getId());
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.PAY)
                .setHeader("orderId", orderId)
                .setHeader("payCode", payCode)
                .build())).blockLast();
        logger.info("after calling pay(): " + stateMachine.getState().getId());
        return get(orderId);
    }

    private synchronized StateMachine<OrderStatus, OrderEvents> getStateMachine(Long orderId) {
        String machineId =  Long.toString(orderId);
        if (currentStateMachine == null) {
            currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);
            currentStateMachine.startReactively().block();
        } else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
            stateMachineService.releaseStateMachine(currentStateMachine.getId());
            currentStateMachine.stopReactively().block();
            currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);
            currentStateMachine.startReactively().block();
        }
        return currentStateMachine;
    }
}
