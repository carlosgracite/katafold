package com.example.todo.state;

import org.immutables.value.Value;

@Value.Immutable
public abstract class TodoItem {

    @Value.Parameter
    public abstract long id();

    @Value.Parameter
    public abstract String text();

    @Value.Default
    public boolean completed() {
        return false;
    }

}
