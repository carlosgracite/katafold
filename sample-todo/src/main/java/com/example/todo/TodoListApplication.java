package com.example.todo;

import android.app.Application;

import com.example.todo.middleware.LoggerMiddleware;
import com.example.todo.middleware.ThunkMiddleware;
import com.example.todo.reducer.KataAppReducer;

public class TodoListApplication extends Application {

    private static AppStore store;

    @Override
    public void onCreate() {
        super.onCreate();
        setupStore();
    }

    private void setupStore() {
        store = new AppStore(new KataAppReducer());
        store.applyMidlewares(
                new LoggerMiddleware(store),
                new ThunkMiddleware<>(store));
        store.createInitialState();
    }

    public static AppStore getStore() {
        return store;
    }
}
