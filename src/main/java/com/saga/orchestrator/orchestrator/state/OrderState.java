package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.model.Issue;

public class OrderState {
    private IOrderState state = new ProductStateI();
    private  Issue issue = new Issue();
    public IOrderState getState() {
        return state;
    }

    public void setState(IOrderState state) {
        this.state = state;
    }

    public  void nextState(Issue issue) {
        state.next(this, this.issue);
    }

    public void printStatus() {
        state.printStatus();
    }
}
