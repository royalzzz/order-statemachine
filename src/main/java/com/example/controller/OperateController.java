package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;

@RestController
@RequestMapping("operate")
public class OperateController {

    @Autowired
    StateMachineFactory<String, String> factory;

    @GetMapping("subscribe")
    public Disposable subscribe() {
        StateMachine<String, String> stateMachine = factory.getStateMachine();
        return stateMachine.startReactively().subscribe();
    }
}
