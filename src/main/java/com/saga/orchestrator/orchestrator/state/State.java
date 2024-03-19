package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.model.Issue;

public class State {
    private IOrderState orderState = new OrderStateI();

    private  boolean validaPrev = true;
    private  Issue issue = new Issue();

    public boolean isValidaPrev() {
        return validaPrev;
    }

    public void setValidaPrev(boolean validaPrev) {
        this.validaPrev = validaPrev;
    }

    public IOrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(IOrderState orderState) {
        this.orderState = orderState;
    }

    public  void nextState(Issue issue) {
        orderState.nextOrderState(this, issue);
    }

    public  void prevState(State state, Issue issue){
        state.prevState(this, issue);
    }

    public String printStatusState() {
        return orderState.printStatusOrderState();
    }
}
