package com.example.todo;

import com.carlosgracite.katafold.Reducer;
import com.carlosgracite.katafold.Store;
import com.example.todo.state.AppState;
import com.example.todo.state.ImmutableAppState;
import com.example.todo.state.TodoItem;

import java.util.Collections;

public class AppStore extends Store<AppState> {

    public AppStore(Reducer<AppState> reducer) {
        super(reducer);
    }

    @Override
    protected ImmutableAppState createInitialState() {
        return ImmutableAppState.builder()
                .todoItems(Collections.<TodoItem>emptyList())
                .build();
    }
}
