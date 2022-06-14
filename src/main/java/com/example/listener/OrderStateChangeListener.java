package com.example.listener;

import com.example.entity.OrderEntity;
import com.example.enums.OrderEvents;
import com.example.enums.OrderStatus;
import com.example.repo.OrderRepo;
import com.example.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderStateChangeListener extends StateMachineListenerAdapter<OrderStatus, OrderEvents> {

    Logger logger = LoggerFactory.getLogger(OrderStateChangeListener.class);

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public void stateContext(StateContext<OrderStatus, OrderEvents> stateContext) {
        if (stateContext.getStage().equals(StateContext.Stage.STATE_CHANGED)) {
            Optional.ofNullable(stateContext.getMessage()).flatMap(
                    msg -> Optional.ofNullable((Long) msg.getHeaders().getOrDefault("orderId", -1L))).ifPresent(
                    orderId1 -> {
                        OrderEntity order = orderRepo.findById(orderId1).orElseThrow();
                        order.setStatus(stateContext.getTarget().getId());
                        orderRepo.save(order);
                        logger.info("change order status: " + stateContext.getTarget().getId());
                    }
            );
        }
    }

    @Override
    public void stateChanged(State<OrderStatus, OrderEvents> from, State<OrderStatus, OrderEvents> to) {
        logger.info(String.format("stateChanged(from: %s, to: %s)", from == null ? null : from.getId() + "", to == null? null :to.getId() + ""));
    }

    @Override
    public void stateEntered(State<OrderStatus, OrderEvents> state) {
    }

    @Override
    public void stateExited(State<OrderStatus, OrderEvents> state) {
    }

    @Override
    public void transition(Transition<OrderStatus, OrderEvents> transition) {
    }

    @Override
    public void transitionStarted(Transition<OrderStatus, OrderEvents> transition) {
    }

    @Override
    public void transitionEnded(Transition<OrderStatus, OrderEvents> transition) {
    }

    @Override
    public void stateMachineStarted(StateMachine<OrderStatus, OrderEvents> stateMachine) {
    }

    @Override
    public void stateMachineStopped(StateMachine<OrderStatus, OrderEvents> stateMachine) {
    }

    @Override
    public void eventNotAccepted(Message<OrderEvents> event) {
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
    }

    @Override
    public void stateMachineError(StateMachine<OrderStatus, OrderEvents> stateMachine, Exception exception) {
    }
}
