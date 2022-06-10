package com.example.controller;

import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.statemachine.data.jpa.JpaRepositoryTransition;
import org.springframework.statemachine.transition.TransitionKind;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("machine")
public class MachineConfigController {

    @Autowired
    StateRepository<JpaRepositoryState> stateRepository;

    @Autowired
    TransitionRepository<JpaRepositoryTransition> transitionRepository;

    @GetMapping("create")
    public void create() {
        JpaRepositoryState WAIT_RECEIVE = new JpaRepositoryState(OrderStatus.WAIT_RECEIVE.name(), true);
        JpaRepositoryState WAIT_PAYMENT = new JpaRepositoryState(OrderStatus.WAIT_PAYMENT.name(), false);
        JpaRepositoryState WAIT_DELIVER = new JpaRepositoryState(OrderStatus.WAIT_DELIVER.name(), false);
        JpaRepositoryState FINISH = new JpaRepositoryState(OrderStatus.FINISH.name(), false);
        stateRepository.saveAll(List.of(WAIT_RECEIVE, WAIT_PAYMENT, WAIT_DELIVER, FINISH));

        JpaRepositoryTransition RECEIVED = new JpaRepositoryTransition(WAIT_RECEIVE, WAIT_PAYMENT, OrderEvents.RECEIVED.name());
        JpaRepositoryTransition PAYED = new JpaRepositoryTransition(WAIT_PAYMENT, WAIT_DELIVER, OrderEvents.PAYED.name());
        JpaRepositoryTransition DELIVERY = new JpaRepositoryTransition(WAIT_DELIVER, FINISH, OrderEvents.DELIVERY.name());
        transitionRepository.saveAll(List.of(RECEIVED, PAYED, DELIVERY));
    }
}
