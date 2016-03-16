package com.carlosgracite.katafold;

import com.carlosgracite.katafold.processor.KatafoldProcessor;
import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;


public class ActionSelectorTest {

    @Test
    public void shouldGenerateMultipleConstructors() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  public TestReducer(String arg0) {}",
                "  public TestReducer(String arg0, Integer arg1) {}",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(String state, Integer action) {return null;}",
                "}"));

        JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/KataTestReducer",
                Joiner.on('\n').join(
                        "package test;",
                        "import com.carlosgracite.katafold.Action;",
                        "import java.lang.Integer;",
                        "import java.lang.Override;",
                        "import java.lang.String;",
                        "public class KataTestReducer extends TestReducer {",
                        "  public KataTestReducer(String arg0) {",
                        "    super(arg0);",
                        "  }",
                        "  public KataTestReducer(String arg0, Integer arg1) {",
                        "    super(arg0, arg1);",
                        "  }",
                        "  @Override",
                        "  public String reduce(String state, Action action) {",
                        "    switch (action.getType()) {",
                        "      case \"ACTION_TEST\":",
                        "        return testAction(state, (Integer)action.getPayload());",
                        "    }",
                        "    return state;",
                        "  }",
                        "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test
    public void shouldCompileWithMultipleInterfaces() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "interface IStub {}",
                "public abstract class TestReducer implements IStub, Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST1\")",
                "  String testActionA(String state, String Action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .compilesWithoutError();
    }

    @Test
    public void shouldCompileWithMultipleActions() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST1\")",
                "  String testActionA(String state, Integer action) {return null;}",
                "",
                "  @ActionSelector(\"ACTION_TEST2\")",
                "  String testActionB(String state, String action) {return null;}",
                "}"));

        JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/KataTestReducer",
                Joiner.on('\n').join(
                        "package test;",
                        "import com.carlosgracite.katafold.Action;",
                        "import java.lang.Integer;",
                        "import java.lang.Override;",
                        "import java.lang.String;",
                        "public class KataTestReducer extends TestReducer {",
                        "  public KataTestReducer() {",
                        "    super();",
                        "  }",
                        "  @Override",
                        "  public String reduce(String state, Action action) {",
                        "    switch (action.getType()) {",
                        "      case \"ACTION_TEST1\":",
                        "        return testActionA(state, (Integer)action.getPayload());",
                        "      case \"ACTION_TEST2\":",
                        "        return testActionB(state, (String)action.getPayload());",
                        "    }",
                        "    return state;",
                        "  }",
                        "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test
    public void shouldGenerateNoPayloadAction() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(String state) {return null;}",
                "}"));

        JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/KataTestReducer",
                Joiner.on('\n').join(
                        "package test;",
                        "import com.carlosgracite.katafold.Action;",
                        "import java.lang.Override;",
                        "import java.lang.String;",
                        "public class KataTestReducer extends TestReducer {",
                        "  public KataTestReducer() {",
                        "    super();",
                        "  }",
                        "  @Override",
                        "  public String reduce(String state, Action action) {",
                        "    switch (action.getType()) {",
                        "      case \"ACTION_TEST\":",
                        "        return testAction(state);",
                        "    }",
                        "    return state;",
                        "  }",
                        "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test
    public void shouldCompileWithMultipleReducers() {
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

        JavaFileObject expectedSource1 = JavaFileObjects.forSourceString("test/KataTestReducer1",
                Joiner.on('\n').join(
                        "package test;",
                        "import com.carlosgracite.katafold.Action;",
                        "import java.lang.Override;",
                        "import java.lang.String;",
                        "public class KataTestReducer1 extends TestReducer1 {",
                        "  public KataTestReducer1() {",
                        "    super();",
                        "  }",
                        "  @Override",
                        "  public String reduce(String state, Action action) {",
                        "    switch (action.getType()) {",
                        "      case \"ACTION_TEST_A\":",
                        "        return testActionA(state, (String)action.getPayload());",
                        "    }",
                        "    return state;",
                        "  }",
                        "}"));

        JavaFileObject expectedSource2 = JavaFileObjects.forSourceString("test/KataTestReducer2",
                Joiner.on('\n').join(
                        "package test;",
                        "import com.carlosgracite.katafold.Action;",
                        "import java.lang.Override;",
                        "import java.lang.String;",
                        "public class KataTestReducer2 extends TestReducer2 {",
                        "  public KataTestReducer2() {",
                        "    super();",
                        "  }",
                        "  @Override",
                        "  public String reduce(String state, Action action) {",
                        "    switch (action.getType()) {",
                        "      case \"ACTION_TEST_B\":",
                        "        return testActionB(state, (String)action.getPayload());",
                        "    }",
                        "    return state;",
                        "  }",
                        "}"));

        Truth.assertAbout(javaSources()).that(Arrays.asList(source1, source2))
                .processedWith(new KatafoldProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource1, expectedSource2);
    }

    @Test
    public void failIfPrivateConstructor() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  private TestReducer() {}",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(String state, Integer action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("class TestReducer contains methods annotated with @ActionSelector and should not contain private constructors.");
    }

    @Test
    public void failIfActionMethodIsPrivate() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST1\")",
                "  private String testActionA(String state, String Action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("testActionA() method should not be private.");
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
                .processedWith(new KatafoldProcessor())
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
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("TestReducer class should implement com.carlosgracite.katafold.Reducer interface.");
    }

    @Test
    public void failsIfClassNotPublic() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  Integer testAction(String state, String action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("class TestReducer contains methods annotated with @ActionSelector and should be public.");
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
                .processedWith(new KatafoldProcessor())
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
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("testAction() method should return String type.");
    }

    @Test
    public void failsIfMethodContainsNoParameter() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction() {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("wrong number of parameters on method testAction().");
    }

    @Test
    public void failsIfMethodContainsMoreThanTwoParameters() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testAction(String arg0, String arg1, String arg2) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
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
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("first argument of method testAction() should be String.");
    }

    @Test
    public void failsIfActionIdAlreadyDefined() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestReducer", Joiner.on('\n').join(
                generateImportBoilerplate(),
                "public abstract class TestReducer implements Reducer<String> {",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testActionA(String state, String Action) {return null;}",
                "",
                "  @ActionSelector(\"ACTION_TEST\")",
                "  String testActionB(String state, String Action) {return null;}",
                "}"));

        Truth.assertAbout(javaSource()).that(source)
                .processedWith(new KatafoldProcessor())
                .failsToCompile()
                .withErrorContaining("The action selector with id 'ACTION_TEST' used on method testActionB() is already defined on method testActionA().");
    }

    private String generateImportBoilerplate() {
        return Joiner.on('\n').join(
                "package test;",
                "import com.carlosgracite.katafold.Reducer;",
                "import com.carlosgracite.katafold.annotations.ActionSelector;");
    }

}
