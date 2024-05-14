package com.saga.orchestrator.orchestrator.state;

import com.saga.orchestrator.orchestrator.model.Issue;

public interface IOrderState {

   void nextOrderState(State state, Issue issue);

    String printStatusOrderState();

    void prevOrderState(State state, Issue issue);
}
