package com.example.todo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.todo.AppStore;
import com.example.todo.R;
import com.example.todo.TodoListApplication;
import com.example.todo.action.TodoActions;
import com.example.todo.state.AppState;
import com.example.todo.state.ImmutableTodoItem;
import com.example.todo.state.TodoItem;
import com.example.todo.ui.base.BaseKataActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoEditorActivity extends BaseKataActivity<AppStore, AppState> {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.edit_todo)
    EditText todoEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_editor);
        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected AppStore injectStore() {
        return TodoListApplication.getStore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                createTodo();
                finish();
                return true;

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createTodo() {
        TodoItem todoItem = ImmutableTodoItem.builder()
                .id(1)
                .text(todoEditText.getText().toString())
                .build();

        getStore().dispatch(TodoActions.CREATOR.addTodo(todoItem));
    }
}
