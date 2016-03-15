package com.carlosgracite.redroid.processor;

import com.carlosgracite.redroid.annotations.ActionSelector;
import com.google.common.base.Joiner;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class ReducerClass {

    public static final String SUFFIX = "Redroid";
    public static final String INTERFACE_REDUCER = "com.carlosgracite.redroid.Reducer";

    private TypeElement reducerClassElement; // AppReducer
    private DeclaredType reducerStateType; // AppState
    private DeclaredType reducerInterface; // Reducer

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
        reducerInterface = getReducerInterface(
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

    public void generateCode(Elements elementsUtils, Filer filer) throws IOException {

        String packageName = elementsUtils.getPackageOf(reducerClassElement).getQualifiedName().toString();
        String className = reducerClassElement.getSimpleName() + SUFFIX;

        List<MethodSpec> constructors = new ArrayList<>();

        for (ExecutableElement executableElement: getConstructors()) {
            MethodSpec.Builder builder = MethodSpec.constructorBuilder();
            List<String> args = new ArrayList<>();

            for (VariableElement param: executableElement.getParameters()) {
                builder.addParameter(TypeName.get(param.asType()), param.getSimpleName().toString(),
                        param.getModifiers().toArray(new Modifier[param.getModifiers().size()]));
                args.add(param.getSimpleName().toString());
            }

            builder.addStatement("super($N)", Joiner.on(',').join(args));
            constructors.add(builder.build());
        }

        ExecutableElement reducerMethod = getMethodFromReducerInterface();
        MethodSpec reduceMethodSpec = MethodSpec.methodBuilder(reducerMethod.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(reducerStateType.asElement().asType()))
                .addParameter(TypeName.get(reducerStateType.asElement().asType()), "state")
                .addParameter(TypeName.get(reducerMethod.getParameters().get(1).asType()), "action")
                .addCode(generateReduceSwitch())
                .addStatement("return state")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(TypeName.get(reducerClassElement.asType()))
                .addMethods(constructors)
                .addMethod(reduceMethodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();

        javaFile.writeTo(filer);
    }

    private CodeBlock generateReduceSwitch() {
        CodeBlock.Builder builder = CodeBlock.builder()
                .beginControlFlow("switch (action.getType())");

        for (ActionSelectorAnnotatedMethod method: methods) {
            builder.add("case $S:\n", method.getAnnotationValue());
            builder.indent();
            builder.addStatement("return $N(state, ($T)action.getPayload())", method.getMethodName(), method.getParam2().asType());
            builder.unindent();
        }

        builder.endControlFlow();

        return builder.build();
    }

    private List<ExecutableElement> getConstructors() {
        List<ExecutableElement> result = new ArrayList<>();
        for (Element element: reducerClassElement.getEnclosedElements()) {
            if (element.getKind().equals(ElementKind.CONSTRUCTOR)) {
                result.add((ExecutableElement) element);
            }
        }
        return result;
    }

    private ExecutableElement getMethodFromReducerInterface() {
        for (Element element: reducerInterface.asElement().getEnclosedElements()) {
            if (element.getKind().equals(ElementKind.METHOD)) {
                return (ExecutableElement) element;
            }
        }

        throw new IllegalArgumentException(String.format("%s interface does not contain the reduce() method",
                INTERFACE_REDUCER));
    }

}
