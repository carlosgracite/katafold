package com.carlosgracite.redroid.processor;

import java.util.HashMap;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class ReducerClassHolder {

    private HashMap<TypeElement, ReducerClass> reducerClassMap;

    public ReducerClassHolder() {
        reducerClassMap = new HashMap<>();
    }

    public void processMethod(ExecutableElement methodElement) {
        TypeElement enclosingClassElement =
                (TypeElement) methodElement.getEnclosingElement();

        ReducerClass reducerClass = reducerClassMap.get(enclosingClassElement);

        if (reducerClass == null) {
            reducerClass = new ReducerClass(enclosingClassElement);
            reducerClassMap.put(enclosingClassElement, reducerClass);
        }

        reducerClass.addMethod(methodElement);
    }


}
