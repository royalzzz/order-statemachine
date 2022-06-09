package com.example.listener;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class OrderStateChangeListener extends StateMachineListenerAdapter<String, String> {

    private final LinkedList<String> messages = new LinkedList<String>();

    public List<String> getMessages() {
        return messages;
    }

    public void resetMessages() {
        messages.clear();
    }

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        System.out.println("stateContext");
        if (stateContext.getStage() == StateContext.Stage.STATE_ENTRY) {
            System.out.println("STATE_ENTRY");
            messages.addFirst(stateContext.getStateMachine().getId() + " enter " + stateContext.getTarget().getId());
        } else if (stateContext.getStage() == StateContext.Stage.STATE_EXIT) {
            System.out.println("STATE_EXIT");
            messages.addFirst(stateContext.getStateMachine().getId() + " exit " + stateContext.getSource().getId());
        }
    }

}
