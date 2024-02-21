package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.model.Issue;

public class OrderState {
    private IOrderState state = new CreateOrderStateI();

    private  boolean validaPrev = true;
    private  Issue issue = new Issue();

    public boolean isValidaPrev() {
        return validaPrev;
    }

    public void setValidaPrev(boolean validaPrev) {
        this.validaPrev = validaPrev;
    }

    public IOrderState getState() {
        return state;
    }

    public void setState(IOrderState state) {
        this.state = state;
    }

    public  void nextState(Issue issue) {
        state.next(this, issue);
    }

    public  void prevState(OrderState state, Issue issue){
        state.prevState(this, issue);
    }

    public String printStatus() {
        return state.printStatus();
    }
}
