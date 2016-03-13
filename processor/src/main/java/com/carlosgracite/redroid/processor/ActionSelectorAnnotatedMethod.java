package com.carlosgracite.redroid.processor;

import com.carlosgracite.redroid.annotations.ActionSelector;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

public class ActionSelectorAnnotatedMethod {

    private ExecutableElement annotatedMethodElement;

    private String qualifiedSuperClassName;
    private String simpleTypeName;
    private String annotationValue;

    public ActionSelectorAnnotatedMethod(ExecutableElement methodElement, DeclaredType reducerStateType) throws IllegalArgumentException {
        this.annotatedMethodElement = methodElement;

        ActionSelector annotation = methodElement.getAnnotation(ActionSelector.class);
        annotationValue = annotation.value();

        //  return type of annotated method should be the one defined by the generic type
        if (!methodElement.getReturnType().equals(reducerStateType)) {
            throw new IllegalArgumentException(String.format("%s() method should return %s type.",
                    methodElement.getSimpleName().toString(), reducerStateType.asElement().getSimpleName().toString()));
        }

        if (methodElement.getParameters().size() != 2) {
            throw new IllegalArgumentException(String.format("wrong number of parameters on method %s().",
                    methodElement.getSimpleName().toString()));
        }

        VariableElement param1 = methodElement.getParameters().get(0);

        if (!param1.asType().equals(reducerStateType)) {
            throw new IllegalArgumentException(String.format("first argument of method %s() should be %s.",
                    methodElement.getSimpleName().toString(), reducerStateType.asElement().getSimpleName().toString()));
        }

        //throw new IllegalArgumentException("lala");
    }

}
