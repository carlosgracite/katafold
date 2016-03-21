package com.example.todo;

import com.carlosgracite.katafold.Reducer;
import com.carlosgracite.katafold.Store;
import com.example.todo.state.ImmutableAppState;
import com.example.todo.state.TodoItem;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Collections;

public class AppStore extends Store<ImmutableAppState> {

    public AppStore(Reducer<ImmutableAppState> reducer) {
        super(reducer);
    }

    @Override
    protected ImmutableAppState createInitialState() {
        return ImmutableAppState.builder()
                .todoItems(Collections.<TodoItem>emptyList())
                .build();
    }
}
