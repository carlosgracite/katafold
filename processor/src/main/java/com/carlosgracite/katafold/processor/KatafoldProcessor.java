package com.carlosgracite.katafold.processor;

import com.carlosgracite.katafold.annotations.ActionSelector;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class KatafoldProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    private ReducerClassHolder reducerClassHolder = new ReducerClassHolder();
    private Set<String> elementsToBeProcessed = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    private int round = 0;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        System.out.println("Round: " + round);
        for (Element element : roundEnvironment.getRootElements()) {
            System.out.printf("%s = %s (%s)\n", element.getSimpleName(), element.asType(), element.asType().getKind());
        }

        for (Element annotatedElement: roundEnvironment.getElementsAnnotatedWith(ActionSelector.class)) {

            // only methods can be annotated with @ActionSelector
            if (annotatedElement.getKind() != ElementKind.METHOD) {
                error(annotatedElement, "Only methods can be annotated with @%s.",
                        ActionSelector.class.getSimpleName());
                return true;
            }

            ExecutableElement executableElement = (ExecutableElement) annotatedElement;

            if (allTypesResolvedFromClass((TypeElement) executableElement.getEnclosingElement())) {
                try {
                    reducerClassHolder.processMethod(executableElement);
                } catch (IllegalArgumentException e) {
                    error(executableElement, e.getMessage());
                    return true;
                }
            } else {
                elementsToBeProcessed.add(((TypeElement) executableElement.getEnclosingElement()).getQualifiedName().toString());
            }
        }

        Iterator<String> iter = elementsToBeProcessed.iterator();
        while (iter.hasNext()) {
            String item = iter.next();

            TypeElement element = elementUtils.getTypeElement(item);

            if (allTypesResolvedFromClass(element)) {
                try {
                    for (Element e: element.getEnclosedElements()) {
                        if (e.getAnnotation(ActionSelector.class) != null) {
                            reducerClassHolder.processMethod((ExecutableElement) e);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    error(element, e.getMessage());
                    return true;
                }
                iter.remove();
            }
        }

        try {
            reducerClassHolder.generateCode(elementUtils, filer);
            reducerClassHolder.clear();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        round++;

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(ActionSelector.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    // =============================================================================================
    //  Helpers
    // =============================================================================================

    private void error(Element element, String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR,
                String.format(message, args), element);
    }

    private boolean allTypesResolved(ExecutableElement executableElement) {
        if (executableElement.getReturnType().getKind() == TypeKind.ERROR) {
            return false;
        }

        for (VariableElement variableElement: executableElement.getParameters()) {
            if (variableElement.asType().getKind() == TypeKind.ERROR) {
                return false;
            }
        }

        return true;
    }

    private boolean allTypesResolvedFromClass(TypeElement typeElement) {
        for (Element e: typeElement.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD && !allTypesResolved((ExecutableElement) e)) {
                return false;
            }
        }
        return true;
    }
}
