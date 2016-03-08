package com.carlosgracite.redroid;

import java.util.ArrayList;
import java.util.List;

public abstract class Store<State> {

    private boolean isDispatching = false;
    private State currentState;
    private Reducer<State> currentReducer;

    private List<ChangeListener<State>> changeListeners = new ArrayList<>();

    public Store(Reducer<State> reducer) {
        this.currentReducer = reducer;
        this.currentState = createInitialState();
    }

    public Action dispatch(Action action) {
        State previousState = currentState;

        try {
            isDispatching = true;
            currentState = currentReducer.reduce(currentState, action);
        } finally {
            isDispatching = false;
        }

        if (!currentState.equals(previousState)) {
            for (ChangeListener<State> changeListener: changeListeners) {
                changeListener.onStateChange(currentState);
            }
        }

        return action;
    }

    protected abstract State createInitialState();

    public void registerChangeListener(ChangeListener<State> listener) {
        changeListeners.add(listener);
    }

    public void unregisterChangeListener(ChangeListener<State> listener) {
        changeListeners.remove(listener);
    }

    public boolean isDispatching() {
        return isDispatching;
    }

    public State getState() {
        return currentState;
    }

    public interface ChangeListener<State> {
        void onStateChange(State currentState);
    }
}
