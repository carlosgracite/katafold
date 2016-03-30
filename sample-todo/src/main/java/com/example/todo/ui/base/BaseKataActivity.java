package com.example.todo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.carlosgracite.katafold.Store;

/**
 * Base class to handle some common operations of a store backed activity. Here the store is
 * injected when the activity is created and the state change listener is registered/unregistered
 * in onResume/onPause callbacks. Also, a state change call is made in onResume to ensure the
 * activity is in the same track as the app state.
 *
 * @param <S>
 * @param <State>
 */
public abstract class BaseKataActivity<S extends Store<State>, State> extends AppCompatActivity
        implements Store.ChangeListener<State> {

    private S store;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = injectStore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        store.registerChangeListener(this);
        onStateChange(store.getState());
    }

    @Override
    protected void onPause() {
        super.onPause();
        store.unregisterChangeListener(this);
    }

    public S getStore() {
        return store;
    }

    public State getState() {
        return store.getState();
    }

    protected abstract S injectStore();

    @Override
    public void onStateChange(State currentState) {

    }
}
