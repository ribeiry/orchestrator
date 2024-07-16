package com.saga.orchestrator.state;

import com.saga.orchestrator.model.Issue;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IOrderState {

   void next(OrderState orderState, Issue issue);

    String printStatus();

    void prevState(OrderState state, Issue issue);
}
