package com.example.todo.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todo.state.TodoItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoItem> todoItemList;

    public TodoAdapter() {
        this.todoItemList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem todoItem = todoItemList.get(position);
        holder.textView.setText(todoItem.text());
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
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
