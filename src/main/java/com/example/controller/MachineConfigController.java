package com.example.controller;

import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.statemachine.data.jpa.JpaRepositoryTransition;
import org.springframework.statemachine.transition.TransitionKind;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("machine")
public class MachineConfigController {

    @Autowired
    StateRepository<JpaRepositoryState> stateRepository;

    @Autowired
    TransitionRepository<JpaRepositoryTransition> transitionRepository;

    @GetMapping("create")
    public void addTransition() {
        String machineId = "orderMachine";
        JpaRepositoryState WAIT_RECEIVE = new JpaRepositoryState(machineId, OrderStatus.WAIT_RECEIVE.name(), true);
        JpaRepositoryState WAIT_PAYMENT = new JpaRepositoryState(machineId, OrderStatus.WAIT_PAYMENT.name(), false);
        JpaRepositoryState WAIT_DELIVER = new JpaRepositoryState(machineId, OrderStatus.WAIT_DELIVER.name(), false);
        JpaRepositoryState FINISH = new JpaRepositoryState(machineId, OrderStatus.FINISH.name(), false);
        stateRepository.saveAll(List.of(WAIT_RECEIVE, WAIT_PAYMENT, WAIT_DELIVER, FINISH));

        JpaRepositoryTransition RECEIVED = new JpaRepositoryTransition(machineId, WAIT_RECEIVE, WAIT_PAYMENT, OrderEvents.RECEIVED.name());
        RECEIVED.setKind(TransitionKind.EXTERNAL);
        JpaRepositoryTransition PAYED = new JpaRepositoryTransition(machineId, WAIT_PAYMENT, WAIT_DELIVER, OrderEvents.PAYED.name());
        PAYED.setKind(TransitionKind.EXTERNAL);
        JpaRepositoryTransition DELIVERY = new JpaRepositoryTransition(machineId, WAIT_DELIVER, FINISH, OrderEvents.DELIVERY.name());
        DELIVERY.setKind(TransitionKind.EXTERNAL);
        transitionRepository.saveAll(List.of(RECEIVED, PAYED, DELIVERY));
    }

    @GetMapping("/{machineId}")
    public List<JpaRepositoryState> addState(@PathVariable("machineId") String machineId) {
        return stateRepository.findByMachineId(machineId);
    }
}
