package com.example.service.impl;

import com.example.entity.OrderEntity;
import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.repo.OrderRepo;
import com.example.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private StateMachineService<OrderStatus, OrderEvents> stateMachineService;

    @Autowired
    private StateMachineFactory<OrderStatus, OrderEvents> stateMachineFactory;

    @Autowired
    private StateMachineRuntimePersister<OrderStatus, OrderEvents, OrderEntity> stateMachineRuntimePersister;

    @Autowired
    private StateMachinePersister<OrderStatus, OrderEvents, OrderEntity> stateMachinePersister;

    @Override
    public OrderEntity get(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }

    @Override
    public OrderEntity create() {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.SUBMITTED);
        order.setCreateTime(new Date().toInstant());
        DefaultStateMachineContext<OrderStatus, OrderEvents> defaultStateMachineContext = new DefaultStateMachineContext(order.getStatus(), null, null, null, null, null);
        stateMachineRuntimePersister.write(defaultStateMachineContext);
        stateMachinePersister.persist();
        stateMachinePersister.restore();
        return orderRepo.save(order);
    }

    @Override
    public OrderEntity fulfill(Long orderId) {
        StateMachine<OrderStatus, OrderEvents> stateMachine = this.build(orderId);
        logger.info("before calling fulfill(): " + stateMachine.getState().getId());
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.FULFILL)
                .setHeader("orderId", orderId)
                .build())).blockLast();
        stateMachine.stopReactively().block();
        logger.info("after calling fulfill(): " + stateMachine.getState().getId());
        return get(orderId);
    }

    @Override
    public OrderEntity pay(Long orderId, String paymentConfirmationNumber) {
        StateMachine<OrderStatus, OrderEvents> stateMachine = this.build(orderId);
        logger.info("before calling pay(): " + stateMachine.getState().getId());
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(OrderEvents.PAY)
                .setHeader("orderId", orderId)
                .setHeader("paymentConfirmationNumber", paymentConfirmationNumber)
                .build())).blockLast();
        stateMachine.stopReactively().block();
        logger.info("after calling pay(): " + stateMachine.getState().getId());
        return get(orderId);
    }

    private StateMachine<OrderStatus, OrderEvents> build(Long orderId) {
        OrderEntity order = orderRepo.findById(orderId).orElseThrow();
        String orderIdKey = Long.toString(order.getId());
        return stateMachineService.acquireStateMachine(orderIdKey, true);
    }
}
