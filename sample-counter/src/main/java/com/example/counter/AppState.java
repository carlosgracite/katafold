package com.example.counter;

import org.immutables.value.Value;

@Value.Immutable
public abstract class AppState {

    @Value.Parameter
    public abstract int getCount();

}
