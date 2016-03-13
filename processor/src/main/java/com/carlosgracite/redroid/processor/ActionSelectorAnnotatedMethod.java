package com.carlosgracite.redroid.processor;

import com.carlosgracite.redroid.annotations.ActionSelector;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class ActionSelectorAnnotatedMethod {

    public static final String INTERFACE_REDUCER = "com.carlosgracite.redroid.Reducer";

    private ExecutableElement annotatedMethodElement;

    private TypeElement enclosingClassElement; // AppReducer
    private DeclaredType reducerGenericType; // AppState

    private String qualifiedSuperClassName;
    private String simpleTypeName;
    private String annotationValue;

    public ActionSelectorAnnotatedMethod(ExecutableElement methodElement) throws IllegalArgumentException {
        this.annotatedMethodElement = methodElement;

        ActionSelector annotation = methodElement.getAnnotation(ActionSelector.class);
        annotationValue = annotation.value();

        enclosingClassElement = (TypeElement) methodElement.getEnclosingElement();

        // verifies if enclosing class is not final
        if (enclosingClassElement.getModifiers().contains(Modifier.FINAL)) {
            throw new IllegalArgumentException(
                    String.format("enclosing class of method %s annotated with @%s should not be final.",
                            methodElement.getSimpleName().toString(), ActionSelector.class.getSimpleName()));
        }

        // extracts Reducer interface
        DeclaredType reducerInterface = getReducerInterface(
                enclosingClassElement.getInterfaces(), INTERFACE_REDUCER);

        // enclosing class should implement Reducer interface
        if (reducerInterface == null) {
            throw new IllegalArgumentException(String.format("%s class should implement %s interface.",
                    enclosingClassElement.getSimpleName().toString(), INTERFACE_REDUCER));
        }

        // extracts the generic type declared at Reducer class
        reducerGenericType = (DeclaredType) reducerInterface.getTypeArguments().get(0);

        //  return type of annotated method should be the one defined by the generic type
        if (!methodElement.getReturnType().equals(reducerGenericType)) {
            throw new IllegalArgumentException(String.format("%s() method should return %s type.",
                    methodElement.getSimpleName().toString(), reducerGenericType.asElement().getSimpleName().toString()));
        }

        if (methodElement.getParameters().size() != 2) {
            throw new IllegalArgumentException(String.format("wrong number of parameters on method %s().",
                    methodElement.getSimpleName().toString()));
        }

        VariableElement param1 = methodElement.getParameters().get(0);

        if (!param1.asType().equals(reducerGenericType)) {
            throw new IllegalArgumentException(String.format("first argument of method %s() should be %s.",
                    methodElement.getSimpleName().toString(), reducerGenericType.asElement().getSimpleName().toString()));
        }

        //throw new IllegalArgumentException("lala");
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
