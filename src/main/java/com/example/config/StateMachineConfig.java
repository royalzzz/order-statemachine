package com.example.config;

import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.listener.OrderStateChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
@EnableStateMachineFactory
class StateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvents> {

    @Autowired
    private OrderStateChangeListener orderStateChangeListener;

    @Autowired
    private StateMachineRuntimePersister<OrderStatus, OrderEvents, String> stateMachineRuntimePersister;

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.SUBMITTED)
                .state(OrderStatus.PAID)
                .end(OrderStatus.FULFILLED)
                .end(OrderStatus.CANCELLED);
    }
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatus.SUBMITTED).target(OrderStatus.PAID).event(OrderEvents.PAY)
                .and()
                .withExternal().source(OrderStatus.PAID).target(OrderStatus.FULFILLED).event(OrderEvents.FULFILL)
                .and()
                .withExternal().source(OrderStatus.SUBMITTED).target(OrderStatus.CANCELLED).event(OrderEvents.CANCEL)
                .and()
                .withExternal().source(OrderStatus.PAID).target(OrderStatus.CANCELLED).event(OrderEvents.CANCEL);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvents> config) throws Exception {
        config
                .withConfiguration()
                .listener(orderStateChangeListener)
                .and()
                .withPersistence()
                .runtimePersister(stateMachineRuntimePersister);
    }
}