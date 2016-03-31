package com.example.todo.action;

import com.carlosgracite.katafold.Action;
import com.carlosgracite.katafold.Store;

public class AsyncAction<S extends Store> extends Action<AsyncAction.Function<S>> {

    public AsyncAction(String type, Function<S> payload) {
        super(type, payload);
    }

    public interface Function<Store> {
        void call(Store store);
    }
}
