package com.carlosgracite.redroid.example;

public interface Reducer<State> {

    State reduce(State state, Action action);

}
