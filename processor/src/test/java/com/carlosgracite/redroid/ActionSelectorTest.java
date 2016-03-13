package com.carlosgracite.redroid;

import com.carlosgracite.redroid.processor.RedroidProcessor;
import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;


public class ActionSelectorTest {

    @Test
    public void multipleReducersCompileWithSuccess() {
        JavaFileObject source1 = JavaFileObjects.forSourceString("test.TestReducer1", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer1 implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST_A\")",
                "  String testActionA(String state, String action) {return null;}",
                "}"));

        JavaFileObject source2 = JavaFileObjects.forSourceString("test.TestReducer2", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer2 implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST_B\")",
                "  String testActionB(String state, String action) {return null;}",
                "}"));

        Truth.assertAbout(javaSources()).that(Arrays.asList(source1, source2))
                .processedWith(new RedroidProcessor())
                .compilesWithoutError();
    }


    @Test
    public void failsIfWrongElementAnnotatedWithActionSelector() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "@ActionSelector(\"ACTION_TEST\")",
                "public abstract class TestReducer implements Reducer<String> {",
                "  String testAction(String state, String action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new RedroidProcessor())
                .failsToCompile()
                .withErrorContaining("Only methods can be annotated with @ActionSelector.");
    }

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
                .withErrorContaining("class TestReducer contains methods annotated with @ActionSelector and should not be final.");
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
