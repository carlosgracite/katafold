package com.example.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.todo.action.TodoActions;
import com.example.todo.reducer.KataAppReducer;
import com.example.todo.state.AppState;
import com.example.todo.state.ImmutableTodoItem;
import com.example.todo.state.TodoItem;
import com.example.todo.ui.TodoAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AppStore.ChangeListener<AppState> {

    @Bind(R.id.list_todo)
    RecyclerView todoRecyclerView;

    private TodoAdapter todoAdapter;

    private AppStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        store = new AppStore(new KataAppReducer());
        onStateChange(store.createInitialState());
    }

    @OnClick(R.id.fab)
    void onAddTodoClick() {
        TodoItem todoItem = ImmutableTodoItem.builder()
                .id(1)
                .text("Todo 1")
                .build();

        store.dispatch(TodoActions.CREATOR.addTodo(todoItem));
    }

    @Override
    protected void onResume() {
        super.onResume();
        store.registerChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        store.unregisterChangeListener(this);
    }

    @Override
    public void onStateChange(AppState currentState) {
        Log.d("LOL", "size: " + store.getState().todoItems());
        if (todoAdapter == null) {
            todoAdapter = new TodoAdapter(currentState.todoItems());
            todoRecyclerView.setAdapter(todoAdapter);
        } else {
            todoAdapter.setTodoItemList(currentState.todoItems());
            todoAdapter.notifyDataSetChanged();
        }
    }
}
