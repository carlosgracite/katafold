package com.example.todo.reducer;

import com.carlosgracite.katafold.Reducer;
import com.carlosgracite.katafold.annotations.ActionSelector;
import com.example.todo.state.ImmutableAppState;
import com.example.todo.state.ImmutableTodoItem;
import com.example.todo.state.TodoItem;
import com.example.todo.util.ImmutableUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;

public abstract class AppReducer implements Reducer<ImmutableAppState> {

    @ActionSelector("ADD_TODO")
    protected ImmutableAppState addTodo(ImmutableAppState state, TodoItem todoItem) {
        return ImmutableAppState.builder()
                .from(state)
                .addTodoItems(todoItem)
                .build();
    }

    @ActionSelector("TOGGLE_TODO")
    protected ImmutableAppState toggleTodo(ImmutableAppState state, final Long id) {
        List<TodoItem> todoItemList = state.todoItems();

        int index = Iterables.indexOf(todoItemList, new Predicate<TodoItem>() {
            @Override
            public boolean apply(TodoItem input) {
                return id == input.id();
            }
        });

        if (index < 0) {
            return state;
        }

        TodoItem todoItem = todoItemList.get(index);
        TodoItem itemToAdd = ImmutableTodoItem.builder()
                .from(todoItem)
                .completed(!todoItem.completed())
                .build();

        return ImmutableAppState.builder()
                .from(state)
                .todoItems(ImmutableUtils.updateItem(todoItemList, itemToAdd, index))
                .build();
    }

}
