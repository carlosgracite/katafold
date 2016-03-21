package com.example.todo.action;

import com.carlosgracite.katafold.Action;
import com.example.todo.state.TodoItem;

public class TodoActions {


    public static class CREATOR {

        public static Action<TodoItem> addTodo(TodoItem todoItem) {
            return new Action<>("ADD_TODO", todoItem);
        }

        public static Action<Long> toggleTodo(long id) {
            return new Action<>("TOGGLE_TODO", id);
        }
    }

}
