package com.example.todo.middleware;

import com.carlosgracite.katafold.Action;
import com.carlosgracite.katafold.Middleware;
import com.carlosgracite.katafold.Store;
import com.example.todo.action.ThunkAction;

public class ThunkMiddleware<S extends Store> extends Middleware<S> {

    public ThunkMiddleware(S store) {
        super(store);
    }

    @Override
    public Action dispatch(Action action) {

        if (action instanceof ThunkAction) {
            ((ThunkAction<S>)action).getPayload().call(getStore());
            return action;
        }

        return next(action);
    }
}
