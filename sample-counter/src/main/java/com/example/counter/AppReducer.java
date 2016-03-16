package com.example.counter;

import com.carlosgracite.katafold.Reducer;
import com.carlosgracite.katafold.annotations.ActionSelector;

public abstract class AppReducer implements Reducer<AppState> {

    @ActionSelector("ACTION_INCREMENT")
    public AppState increment(AppState state) {
        return ImmutableAppState.builder()
                .from(state)
                .count(state.getCount() + 1)
                .build();
    }

    @ActionSelector("ACTION_DECREMENT")
    public AppState decrement(AppState state) {
        return ImmutableAppState.builder()
                .from(state)
                .count(state.getCount() - 1)
                .build();
    }

}
