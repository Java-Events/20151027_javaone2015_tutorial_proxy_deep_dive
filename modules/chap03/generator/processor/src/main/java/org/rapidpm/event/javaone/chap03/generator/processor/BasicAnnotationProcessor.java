package org.rapidpm.event.javaone.chap03.generator.processor;

import com.google.common.base.Joiner;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Created by svenruppert on 21.10.15.
 */
public abstract class BasicAnnotationProcessor<T  extends Annotation> extends AbstractProcessor {

  public abstract Class<T> responsibleFor();

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotataions = new LinkedHashSet<>();
    annotataions.add(responsibleFor().getCanonicalName());
    return annotataions;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  protected Filer filer;
  protected Messager messager;
  protected Elements elementUtils;
  protected Types typeUtils;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    typeUtils = processingEnv.getTypeUtils();
    elementUtils = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  protected abstract CodeBlock defineMethodImplementation(final ExecutableElement methodElement , final String methodName2Delegate);

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    roundEnv
        .getElementsAnnotatedWith(responsibleFor())
        .stream()
        .filter(e -> e.getKind() == ElementKind.INTERFACE)
        .map(e -> (TypeElement) e)
        .forEach(typeElement -> {
          final TypeName interface2Implement = TypeName.get(typeElement.asType());
          createTypeSpecBuilderForTargetClass(typeElement, interface2Implement);

          addClassLevelSpecs(typeElement,roundEnv);

          //iter over the Methods from the Interface
          typeElement
              .getEnclosedElements()
              .stream()
              .filter(e -> e.getKind() == ElementKind.METHOD)
              .map(methodElement -> (ExecutableElement) methodElement) //cast only
              .filter(methodElement -> methodElement.getModifiers().contains(Modifier.PUBLIC))
              .forEach(methodElement -> {

                final String methodName2Delegate = methodElement.getSimpleName().toString();

                final CodeBlock codeBlock = defineMethodImplementation(methodElement, methodName2Delegate);

                final MethodSpec delegatedMethodSpec = defineDelegatorMethod(methodElement, methodName2Delegate, codeBlock);

                typeSpecBuilderForTargetClass.addMethod(delegatedMethodSpec);
              });

          writeDefinedClass(pkgName(typeElement), typeSpecBuilderForTargetClass);
        });
    return true;
  }

  protected abstract void addClassLevelSpecs(final TypeElement typeElement, final RoundEnvironment roundEnv);

  private TypeSpec.Builder typeSpecBuilderForTargetClass;

  protected TypeSpec.Builder createTypeSpecBuilderForTargetClass(final TypeElement typeElement, final TypeName interface2Implement) {
    if (typeSpecBuilderForTargetClass == null){
      typeSpecBuilderForTargetClass = TypeSpec
          .classBuilder(targetClassNameSimple(typeElement))
          .addSuperinterface(interface2Implement)
          .addModifiers(Modifier.PUBLIC);
    }
    return typeSpecBuilderForTargetClass;
  }


  protected void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }

  protected String pkgName(final Element typeElement) {
    return elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
  }

  protected String targetClassNameSimple(final TypeElement typeElement) {
    return ClassName.get(pkgName(typeElement), className(typeElement) + classNamePostFix()).simpleName();
  }

  private String classNamePostFix() {
    return responsibleFor().getSimpleName();
  }

  protected String className(final Element typeElement) {
    return typeElement.getSimpleName().toString();
  }

  protected FieldSpec defineDelegatorField(final TypeElement typeElement) {
    final ClassName delegatorClassName = ClassName.get(pkgName(typeElement), className(typeElement));
    return FieldSpec
        .builder(delegatorClassName, "delegator")
        .addModifiers(Modifier.PRIVATE)
        .build();
  }

  protected MethodSpec defineDelegatorMethod(final ExecutableElement methodElement, final String methodName2Delegate, final CodeBlock codeBlock) {
    final Set<Modifier> reducedMethodModifiers = new HashSet<>(methodElement.getModifiers());
    reducedMethodModifiers.remove(Modifier.ABSTRACT);

    return MethodSpec.methodBuilder(methodName2Delegate)
        .addModifiers(reducedMethodModifiers)
        .returns(TypeName.get(methodElement.getReturnType()))
        .addParameters(defineParamsForMethod(methodElement))
        .addCode(codeBlock)
        .build();
  }

  public List<ParameterSpec> defineParamsForMethod(final ExecutableElement methodElement) {
    return methodElement
        .getParameters()
        .stream()
        .map(parameter -> {
          final Name simpleName = parameter.getSimpleName();
          final TypeMirror typeMirror = parameter.asType();
          TypeName typeName = TypeName.get(typeMirror);
          return ParameterSpec.builder(typeName, simpleName.toString(), Modifier.FINAL).build();
        })
        .collect(toList());
  }


  public String delegatorStatementWithReturn(final ExecutableElement methodElement, final String methodName2Delegate) {
    return "return delegator." + delegatorMethodCall(methodElement, methodName2Delegate);
  }

  public String delegatorMethodCall(final ExecutableElement methodElement, final String methodName2Delegate) {
    return methodName2Delegate + "(" +
        Joiner.on(", ")
            .skipNulls()
            .join(defineParamsForMethod(methodElement)
                .stream()
                .map(v -> v.name)
                .collect(toList())) +
        ")";
  }



  protected void writeDefinedClass(String pkgName, TypeSpec.Builder typeSpecBuilder) {
    final TypeSpec typeSpec = typeSpecBuilder.build();
    final JavaFile javaFile = JavaFile.builder(pkgName, typeSpec).skipJavaLangImports(true).build();
    final String className = javaFile.packageName + "." + javaFile.typeSpec.name;
    try {
      JavaFileObject jfo = filer.createSourceFile(className);
      Writer writer = jfo.openWriter();
      javaFile.writeTo(writer);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("e = " + e);
    }
  }
}
