package com.example.todo.state;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class AppState {

    @Value.Parameter
    public abstract List<TodoItem> todoItems();

}
