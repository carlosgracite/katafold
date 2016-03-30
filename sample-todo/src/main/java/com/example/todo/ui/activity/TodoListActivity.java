package com.example.todo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.todo.AppStore;
import com.example.todo.R;
import com.example.todo.TodoListApplication;
import com.example.todo.state.AppState;
import com.example.todo.ui.TodoAdapter;
import com.example.todo.ui.base.BaseKataActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodoListActivity extends BaseKataActivity<AppStore, AppState> {

    private static final String TAG = TodoListActivity.class.getSimpleName();

    @Bind(R.id.list_todo)
    RecyclerView todoRecyclerView;

    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        ButterKnife.bind(this);

        todoAdapter = new TodoAdapter(getState().todoItems());

        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);
    }

    @Override
    protected AppStore injectStore() {
        return TodoListApplication.getStore();
    }

    @OnClick(R.id.fab)
    void onAddTodoClick() {
        showTodoEditorScreen();
    }

    @Override
    public void onStateChange(AppState currentState) {
        todoAdapter.setTodoItemList(currentState.todoItems());
        todoAdapter.notifyDataSetChanged();
    }

    private void showTodoEditorScreen() {
        Intent intent = new Intent(this, TodoEditorActivity.class);
        startActivity(intent);
    }
}
