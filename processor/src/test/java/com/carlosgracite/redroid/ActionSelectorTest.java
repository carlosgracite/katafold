package com.carlosgracite.redroid;

import com.carlosgracite.redroid.processor.RedroidProcessor;
import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;


public class ActionSelectorTest {

    @Test
    public void failsIfNotImplementsReducerInterface() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(String state, String action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new RedroidProcessor())
                .failsToCompile()
                .withErrorContaining("TestReducer class should implement com.carlosgracite.redroid.Reducer interface.");
    }

    @Test
    public void failsIfClassFinal() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public final class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  Integer testAction(String state, String action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new RedroidProcessor())
                .failsToCompile()
                .withErrorContaining("enclosing class of method testAction annotated with @ActionSelector should not be final");
    }

    @Test
    public void failsIfReturnTypeWrong() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\") ",
                "  Integer testAction(String state, String action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new RedroidProcessor())
                .failsToCompile()
                .withErrorContaining("testAction() method should return String type.");
    }

    @Test
    public void failsIfMethodContainsWrongNumberOfParameters() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(String state) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new RedroidProcessor())
                .failsToCompile()
                .withErrorContaining("wrong number of parameters on method testAction().");
    }

    @Test
    public void failsIfFirstParameterTypeIsNotTheSameDefinedByReducerInterface() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(Integer state, String Action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new RedroidProcessor())
                .failsToCompile()
                .withErrorContaining("first argument of method testAction() should be String.");
    }

    private String generateImportBoilerplate() {
        return Joiner.on('\n').join(
                "package test;",
                "import com.carlosgracite.redroid.Reducer;",
                "import com.carlosgracite.redroid.annotations.ActionSelector;");
    }

}
