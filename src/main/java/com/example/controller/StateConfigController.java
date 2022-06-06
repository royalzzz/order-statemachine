package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("state")
public class StateConfigController {

    @Autowired
    StateRepository<JpaRepositoryState> stateRepository;

    @GetMapping("add")
    public JpaRepositoryState addState(String machineId, String state, Boolean initial) {
        JpaRepositoryState stateS1 = new JpaRepositoryState(machineId, state, initial);
        return stateRepository.save(stateS1);
    }

    @GetMapping("/{machineId}")
    public List<JpaRepositoryState> addState(@PathVariable("machineId") String machineId) {
        return stateRepository.findByMachineId(machineId);
    }
}
