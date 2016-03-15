package com.carlosgracite.redroid.processor;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class ReducerClassHolder {

    private HashMap<String, ReducerClass> reducerClassMap;

    public ReducerClassHolder() {
        reducerClassMap = new HashMap<>();
    }

    public void processMethod(ExecutableElement methodElement) {
        TypeElement enclosingClassElement =
                (TypeElement) methodElement.getEnclosingElement();

        ReducerClass reducerClass = reducerClassMap.get(enclosingClassElement.getQualifiedName().toString());

        if (reducerClass == null) {
            reducerClass = new ReducerClass(enclosingClassElement);
            reducerClassMap.put(enclosingClassElement.getQualifiedName().toString(), reducerClass);
        }

        reducerClass.addMethod(methodElement);
    }

    public void generateCode(Elements elementsUtils, Filer filer) throws IOException {
        for (ReducerClass reducerClass: reducerClassMap.values()) {
            reducerClass.generateCode(elementsUtils, filer);
        }
    }

    public void clear() {
        reducerClassMap.clear();
    }
}
