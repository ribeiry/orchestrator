package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.model.Issue;

public interface IOrderState {

   void next(OrderState orderState, Issue issue);

    String printStatus();

    void prevState(OrderState state, Issue issue);
}
