package com.example.todo.action;

import com.carlosgracite.katafold.Action;
import com.example.todo.AppStore;
import com.example.todo.state.TodoItem;

public class TodoActions {

    public static final String ADD_TODO = "ADD_TODO";
    public static final String TOGGLE_TODO = "TOGGLE_TODO";

    public static class CREATOR {

        public static Action<TodoItem> addTodo(TodoItem todoItem) {
            return new Action<>(ADD_TODO, todoItem);
        }

        public static Action<Long> toggleTodo(long id) {
            return new Action<>(TOGGLE_TODO, id);
        }

        public static ThunkAction<AppStore> thunkTest() {
            return new ThunkAction<>("THUNK_TEST", new ThunkAction.Function<AppStore>() {
                @Override
                public void call(AppStore store) {
                    // do stuff here
                }
            });
        }
    }

}
