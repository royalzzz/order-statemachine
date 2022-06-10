package com.example.listener;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class OrderStateChangeListener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        System.out.println("Stage:" + stateContext.getStage() + "\t" + stateContext.getMessage());
    }

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        System.out.println("stateChanged:");
    }

    @Override
    public void stateEntered(State<String, String> state) {
        System.out.println("stateEntered:" + state.getId());
    }

    @Override
    public void stateExited(State<String, String> state) {
        System.out.println("stateExited:" + state.getId());
    }

    @Override
    public void transition(Transition<String, String> transition) {
        System.out.println("transition:" + transition.getName());
    }

    @Override
    public void transitionStarted(Transition<String, String> transition) {
        System.out.println("transitionStarted:" + transition.getName());
    }

    @Override
    public void transitionEnded(Transition<String, String> transition) {
        System.out.println("transitionEnded:" + transition.getName());
    }

    @Override
    public void stateMachineStarted(StateMachine<String, String> stateMachine) {
        System.out.println("stateMachineStarted:" + stateMachine.getId());
    }

    @Override
    public void stateMachineStopped(StateMachine<String, String> stateMachine) {
        System.out.println("stateMachineStopped:" + stateMachine.getId());
    }

    @Override
    public void eventNotAccepted(Message<String> event) {
        System.out.println("eventNotAccepted:" + event.getPayload() + "\t" + event.getHeaders());
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        System.out.println("extendedStateChanged:" + key + "\t" +value);
    }

    @Override
    public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
        System.out.println("stateMachineError:" + stateMachine.getId());
    }
}
