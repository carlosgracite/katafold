package com.carlosgracite.redroid;

public interface Reducer<State> {

    State reduce(State state, Action action);

}
