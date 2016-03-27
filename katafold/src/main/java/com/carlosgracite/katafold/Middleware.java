package com.carlosgracite.katafold;

public abstract class Middleware<S extends Store> implements Dispatcher {

    private Dispatcher next;
    private S store;

    public Middleware(S store) {
        this.store = store;
    }

    public void setNext(Dispatcher next) {
        this.next = next;
    }

    public S getStore() {
        return store;
    }

    public Action next(Action action) {
        return next.dispatch(action);
    }
}
