package com.carlosgracite.redroid.example;

public class Store<State> {

    private boolean isDispatching = false;
    private State currentState;
    private Reducer<State> currentReducer;

    public Action dispatch(Action action) {
        try {
            isDispatching = true;
            currentState = currentReducer.reduce(currentState, action);
        } finally {
            isDispatching = false;
        }

        // TODO: call listeners
        return action;
    }
}
