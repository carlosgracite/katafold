package com.example.counter;

import com.carlosgracite.katafold.Action;
import com.carlosgracite.katafold.Reducer;
import com.carlosgracite.katafold.annotations.ActionSelector;

public class AppReducer implements Reducer<AppState> {

    @ActionSelector("ACTION_INCREMENT")
    public AppState increment(AppState appState, Action<Void>action) {
        return null;
    }

    @Override public AppState reduce(AppState state, Action action) {

        switch (action.getType()) {

            case "ACTION_INCREMENT":
                return ImmutableAppState.builder()
                        .from(state)
                        .count(state.getCount() + 1)
                        .build();

            case "ACTION_DECREMENT":
                return ImmutableAppState.builder()
                        .from(state)
                        .count(state.getCount() - 1)
                        .build();
        }

        return state;
    }

}
