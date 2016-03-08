package com.example.counter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carlosgracite.redroid.Store;

public class MainActivity extends AppCompatActivity implements Store.ChangeListener<AppState> {

    private TextView countTextView;
    private Button decrementButton;
    private Button incrementButton;

    private AppStore appStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = (TextView) findViewById(R.id.text_count);
        decrementButton = (Button) findViewById(R.id.button_decrement);
        incrementButton = (Button) findViewById(R.id.button_increment);

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appStore.dispatch(CountAction.CREATOR.decrement());
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appStore.dispatch(CountAction.CREATOR.increment());
            }
        });

        appStore = new AppStore(new AppReducer());

        // setups view with initial state
        onStateChange(appStore.getState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        appStore.registerChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appStore.unregisterChangeListener(this);
    }

    @Override
    public void onStateChange(AppState currentState) {
        countTextView.setText(String.valueOf(currentState.getCount()));
    }
}
