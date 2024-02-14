package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.model.Issue;

public class OrderState {
    private IOrderState state = new CreateOrderStateI();

    private  boolean validaPrev = true;
    private  Issue issue = new Issue();
    public IOrderState getState() {
        return state;
    }

    public void setState(IOrderState state) {
        this.state = state;
    }

    public  void nextState(Issue issue, boolean validaPrev) {
        state.next(this, issue, validaPrev);
    }

    public  void prevState(OrderState state, Issue issue, boolean validaPrev){
        state.prevState(this, issue, validaPrev);
    }

    public String printStatus() {
        return state.printStatus();
    }
}
