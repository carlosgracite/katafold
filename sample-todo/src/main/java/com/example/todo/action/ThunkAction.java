package com.example.todo.action;

import com.carlosgracite.katafold.Action;
import com.carlosgracite.katafold.Store;

public class ThunkAction<S extends Store> extends Action<ThunkAction.Function<S>> {

    public ThunkAction(String type, Function<S> payload) {
        super(type, payload);
    }

    public interface Function<Store> {
        void call(Store store);
    }
}
