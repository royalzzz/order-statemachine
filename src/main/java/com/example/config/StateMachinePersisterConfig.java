package com.example.config;

import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class StateMachinePersisterConfig {

    @Bean
    public StateMachineRuntimePersister<OrderStatus, OrderEvents, String> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineService<OrderStatus, OrderEvents> stateMachineService(
            StateMachineFactory<OrderStatus, OrderEvents> stateMachineFactory,
            StateMachineRuntimePersister<OrderStatus, OrderEvents, String> stateMachineRuntimePersister) {
        return new DefaultStateMachineService<OrderStatus, OrderEvents>(stateMachineFactory, stateMachineRuntimePersister);
    }
}