package com.carlosgracite.katafold;

public class Action<T> {

    private String type;

    private T payload;

    public Action(String type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }

}
