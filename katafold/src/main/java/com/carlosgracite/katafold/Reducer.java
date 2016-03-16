package com.carlosgracite.katafold;

public interface Reducer<State> {

    State reduce(State state, Action action);

}
