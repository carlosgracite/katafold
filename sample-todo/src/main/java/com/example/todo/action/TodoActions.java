package com.example.todo.action;

import android.os.AsyncTask;

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

        public static ThunkAction<AppStore> addTodoAsync(final TodoItem todoItem) {

            return new ThunkAction<>("ADD_TODO_ASYNC", new ThunkAction.Function<AppStore>() {
                @Override
                public void call(final AppStore store) {
                    new AsyncTask<TodoItem, Void, TodoItem>() {
                        @Override
                        protected TodoItem doInBackground(TodoItem[] params) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return params[0];
                        }

                        @Override
                        protected void onPostExecute(TodoItem item) {
                            super.onPostExecute(item);
                            store.dispatch(new Action<>(ADD_TODO, item));
                        }
                    }.execute(todoItem);
                }
            });
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
