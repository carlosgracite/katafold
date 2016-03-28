package com.example.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.todo.action.TodoActions;
import com.example.todo.state.AppState;
import com.example.todo.state.ImmutableTodoItem;
import com.example.todo.state.TodoItem;
import com.example.todo.ui.TodoAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AppStore.ChangeListener<AppState> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.list_todo)
    RecyclerView todoRecyclerView;

    private TodoAdapter todoAdapter;

    private AppStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        store = TodoListApplication.getStore();

        todoAdapter = new TodoAdapter(store.getState().todoItems());

        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);
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
        Log.d(TAG, "size: " + store.getState().todoItems());
        todoAdapter.setTodoItemList(currentState.todoItems());
        todoAdapter.notifyDataSetChanged();
    }
}
