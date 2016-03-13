package com.carlosgracite.redroid.processor;

import com.carlosgracite.redroid.annotations.ActionSelector;
import com.google.auto.service.AutoService;

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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RedroidProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        ReducerClassHolder reducerClassHolder = new ReducerClassHolder();

        for (Element annotatedElement: roundEnvironment.getElementsAnnotatedWith(ActionSelector.class)) {

            // only menthods can be annotated with @ActionSelector
            if (annotatedElement.getKind() != ElementKind.METHOD) {
                error(annotatedElement, "Only methods can be annotated with @%s.",
                        ActionSelector.class.getSimpleName());
                return true;
            }

            ExecutableElement executableElement = (ExecutableElement) annotatedElement;

            try {
                reducerClassHolder.processMethod(executableElement);

            } catch (IllegalArgumentException e) {
                error(executableElement, e.getMessage());
                return true;
            }

        }

        return false;
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
}
