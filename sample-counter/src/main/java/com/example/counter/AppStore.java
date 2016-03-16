package com.example.counter;

import com.carlosgracite.katafold.Reducer;
import com.carlosgracite.katafold.Store;

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
