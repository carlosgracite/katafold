package com.carlosgracite.redroid.processor;

import com.carlosgracite.redroid.annotations.ActionSelector;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

public class ActionSelectorAnnotatedMethod {

    private ExecutableElement annotatedMethodElement;
    private VariableElement param1;
    private VariableElement param2;

    private String annotationValue;
    private String methodName;

    public ActionSelectorAnnotatedMethod(ExecutableElement methodElement, DeclaredType reducerStateType) throws IllegalArgumentException {
        this.annotatedMethodElement = methodElement;
        this.methodName = methodElement.getSimpleName().toString();

        ActionSelector annotation = methodElement.getAnnotation(ActionSelector.class);
        annotationValue = annotation.value();

        // checks if method is private
        if (methodElement.getModifiers().contains(Modifier.PRIVATE)) {
            throw new IllegalArgumentException(String.format("%s() method should not be private.",
                    methodElement.getSimpleName().toString()));
        }

        //  return type of annotated method should be the one defined by the generic type
        if (!methodElement.getReturnType().equals(reducerStateType)) {
            throw new IllegalArgumentException(String.format("%s() method should return %s type.",
                    methodElement.getSimpleName().toString(), reducerStateType.asElement().getSimpleName().toString()));
        }

        if (methodElement.getParameters().size() != 2) {
            throw new IllegalArgumentException(String.format("wrong number of parameters on method %s().",
                    methodElement.getSimpleName().toString()));
        }

        param1 = methodElement.getParameters().get(0);

        if (!param1.asType().equals(reducerStateType)) {
            throw new IllegalArgumentException(String.format("first argument of method %s() should be %s.",
                    methodElement.getSimpleName().toString(), reducerStateType.asElement().getSimpleName().toString()));
        }

        param2 = methodElement.getParameters().get(1);

        //throw new IllegalArgumentException("lala");
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    public String getMethodName() {
        return methodName;
    }
}
