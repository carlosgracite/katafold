package com.example.todo.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.example.todo.AppStore;
import com.example.todo.R;
import com.example.todo.action.TodoActions;
import com.example.todo.state.TodoItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoItem> todoItemList;
    private AppStore store;

    public TodoAdapter(AppStore store, List<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
        this.store = store;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_todo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TodoItem todoItem = todoItemList.get(position);

        holder.textView.setText(todoItem.text());
        holder.textView.setChecked(todoItem.completed());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.dispatch(TodoActions.CREATOR.toggleTodo(todoItem.id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoItemList.size();
    }

    public void setTodoItemList(List<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1)
        CheckedTextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
