package com.example.counter;

import com.carlosgracite.katafold.Action;

public class CountAction extends Action<Void> {

    private CountAction(String type) {
        super(type, null);
    }

    public static class CREATOR {

        public static CountAction increment() {
            return new CountAction("ACTION_INCREMENT");
        }

        public static CountAction decrement() {
            return new CountAction("ACTION_DECREMENT");
        }

    }
}
