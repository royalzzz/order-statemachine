package com.example.controller;

import com.example.entity.OrderEntity;
import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("operate")
public class OperateController {

    @Autowired
    StateMachineFactory<String, String> stateMachineFactory;

    @GetMapping("send")
    public void subscribe(String id) {
        StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine(id);
        Map<String , Object> headers = new HashMap<>();
        stateMachine.startReactively().block();
        stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload(OrderEvents.RECEIVED.name()).setHeader("header", headers).build()))
                .blockLast();
        stateMachine.stopReactively().block();
    }

}
