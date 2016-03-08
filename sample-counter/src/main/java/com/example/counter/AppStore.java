package com.example.counter;

import com.carlosgracite.redroid.Reducer;
import com.carlosgracite.redroid.Store;

public class AppStore extends Store<AppState> {

    public AppStore(Reducer<AppState> reducer) {
        super(reducer);
    }

    @Override
    protected AppState createInitialState() {
        return ImmutableAppState.builder()
                .count(0)
                .build();
    }
}
