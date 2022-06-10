package com.example.controller;

import com.example.entity.OrderEntity;
import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OperateController {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    StateRepository<JpaRepositoryState> stateRepository;

    @Autowired
    private StateMachineService<String, String> stateMachineService;

    @Autowired
    private StateMachinePersist<String, String, String> stateMachinePersist;

    @GetMapping("create")
    @Transactional
    public Object createOrder() {
        String uuid = UUID.randomUUID().toString();
        StateMachine<String, String> stateMachine = stateMachineService.acquireStateMachine(uuid, true);
        return stateMachine.getId();
    }

    @GetMapping("get")
    public Object getOrder(String uuid) {
        StateMachine<String, String> stateMachine = stateMachineService.acquireStateMachine(UUID.fromString(uuid).toString());
        return stateMachine;
    }

    @GetMapping("receive")
    public void receive(Long orderId) {
        StateMachine<String, String> stateMachine = stateMachineService.acquireStateMachine("orderMachine:" + orderId, true);
        if (stateMachine == null) {
            System.out.println("stateMachine is null");
        } else {
            Map<String, Object> headers = new HashMap<>();
            headers.put("orderId", orderId);
            headers.put("hello", "world");
            stateMachine.startReactively().block();
            stateMachine
                    .sendEvent(Mono.just(MessageBuilder
                            .withPayload(OrderEvents.RECEIVED.name()).setHeader("header", headers).build()))
                    .blockLast();
            stateMachine.stopReactively().block();
        }
    }
}
