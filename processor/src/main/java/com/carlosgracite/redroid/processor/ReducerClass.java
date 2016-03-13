package com.carlosgracite.redroid.processor;

import com.carlosgracite.redroid.annotations.ActionSelector;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class ReducerClass {

    public static final String INTERFACE_REDUCER = "com.carlosgracite.redroid.Reducer";

    private TypeElement reducerClassElement; // AppReducer
    private DeclaredType reducerStateType; // AppState

    private List<ActionSelectorAnnotatedMethod> methods;

    public ReducerClass(TypeElement reducerClassElement) {
        this.reducerClassElement = reducerClassElement;
        this.methods = new ArrayList<>();

        // verifies if enclosing class is not final
        if (reducerClassElement.getModifiers().contains(Modifier.FINAL)) {
            throw new IllegalArgumentException(
                    String.format("class %s contains methods annotated with @%s and should not be final.",
                            reducerClassElement.getSimpleName().toString(), ActionSelector.class.getSimpleName()));
        }

        // verifies if enclosing class is not public
        if (!reducerClassElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new IllegalArgumentException(
                    String.format("class %s contains methods annotated with @%s and should be public.",
                            reducerClassElement.getSimpleName().toString(), ActionSelector.class.getSimpleName()));
        }

        // extracts Reducer interface
        DeclaredType reducerInterface = getReducerInterface(
                reducerClassElement.getInterfaces(), INTERFACE_REDUCER);

        // enclosing class should implement Reducer interface
        if (reducerInterface == null) {
            throw new IllegalArgumentException(String.format("%s class should implement %s interface.",
                    reducerClassElement.getSimpleName().toString(), INTERFACE_REDUCER));
        }

        // extracts the generic type declared at Reducer class
        reducerStateType = (DeclaredType) reducerInterface.getTypeArguments().get(0);

    }

    public void addMethod(ExecutableElement methodElement) {
        ActionSelectorAnnotatedMethod method =
                new ActionSelectorAnnotatedMethod(methodElement, reducerStateType);

        ActionSelectorAnnotatedMethod existing = getMethodWithActionId(method.getAnnotationValue());
        if (existing != null) {
            throw new IllegalArgumentException(String.format(
                    "The action selector with id '%s' used on method %s() is already defined on method %s().",
                    method.getAnnotationValue(), method.getMethodName(), existing.getMethodName()));
        }

        methods.add(method);
    }

    public ActionSelectorAnnotatedMethod getMethodWithActionId(String actionId) {
        for (ActionSelectorAnnotatedMethod method: methods) {
            if (method.getAnnotationValue().equals(actionId)) {
                return method;
            }
        }
        return null;
    }

    private DeclaredType getReducerInterface(List<? extends TypeMirror> interfaces, String name) {
        for (TypeMirror typeMirror: interfaces) {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            if (declaredType.asElement().toString().equals(name)) {
                return declaredType;
            }
        }
        return null;
    }
}
