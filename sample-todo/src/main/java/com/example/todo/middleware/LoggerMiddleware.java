package com.example.todo.middleware;

import android.util.Log;

import com.carlosgracite.katafold.Action;
import com.carlosgracite.katafold.Middleware;
import com.carlosgracite.katafold.Store;

public class LoggerMiddleware extends Middleware<Store> {

    private static final String TAG = LoggerMiddleware.class.getSimpleName();

    public LoggerMiddleware(Store store) {
        super(store);
    }

    @Override
    public Action dispatch(Action action) {
        Log.d(TAG, action + " - " + getStore().getState());
        Action result = next(action);
        Log.d(TAG, result + " - " + getStore().getState());
        return result;
    }

}
