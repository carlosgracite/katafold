package com.example.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.todo.action.TodoActions;
import com.example.todo.reducer.KataAppReducer;
import com.example.todo.state.ImmutableTodoItem;
import com.example.todo.state.TodoItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppStore store = new AppStore(new KataAppReducer());

        TodoItem todoItem = ImmutableTodoItem.builder()
                .id(1)
                .text("Todo 1")
                .build();

        store.dispatch(TodoActions.CREATOR.addTodo(todoItem));

        store.dispatch(TodoActions.CREATOR.toggleTodo(1));

        // Should be true
        Log.d("LOL", store.getState().todoItems().get(0).completed() + "");
    }
}
