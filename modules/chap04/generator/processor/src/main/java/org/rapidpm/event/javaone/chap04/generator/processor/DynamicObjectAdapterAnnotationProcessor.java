package org.rapidpm.event.javaone.chap04.generator.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.rapidpm.event.javaone.chap04.generator.annotations.dynamicobjectadapter.AdapterBuilder;
import org.rapidpm.event.javaone.chap04.generator.annotations.dynamicobjectadapter.DynamicObjectAdapterBuilder;
import org.rapidpm.event.javaone.chap04.generator.annotations.dynamicobjectadapter.ExtendedInvocationHandler;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;

/**
 * Created by sven on 13.05.15.
 */

@AutoService(Processor.class)
public class DynamicObjectAdapterAnnotationProcessor extends BasicObjectAdapterAnnotationProcessor<DynamicObjectAdapterBuilder> {

  public static final String INVOCATION_HANDLER_CLASSNAME_POST_FIX = "InvocationHandler";
  private static final String BUILDER_CLASSNAME_POST_FIX = "AdapterBuilder";

  @Override
  public Class<DynamicObjectAdapterBuilder> responsibleFor() {
    return DynamicObjectAdapterBuilder.class;
  }

  @Override
  protected CodeBlock defineMethodImplementation(final ExecutableElement methodElement, final String methodName2Delegate) {
    return null;
  }

  @Override
  protected void addClassLevelSpecs(final TypeElement typeElement, final RoundEnvironment roundEnv) {

  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(responsibleFor())) {
      //Service
      // Check if an interface has been annotated with @Factory
      if (annotatedElement.getKind() != ElementKind.INTERFACE) {
        error(annotatedElement, "Only interfaces can be annotated with @%s", responsibleFor().getSimpleName());
        return true; // Exit processing
      }
      // We can cast it, because we know that it of ElementKind.INTERFACE
      TypeElement typeElement = (TypeElement) annotatedElement;
      final String pkgName = typeElement.getEnclosingElement().toString();
      final ClassName typeElementClassName = ClassName.get(pkgName, typeElement.getSimpleName().toString());

//      final TypeSpec.Builder invocationHandlerBuilder = createInvocationHandlerTypeSpecBuilder(typeElement);
      final TypeSpec.Builder invocationHandlerBuilder = createTypedTypeSpecBuilder(typeElement, ExtendedInvocationHandler.class, INVOCATION_HANDLER_CLASSNAME_POST_FIX);

      final ClassName adapterBuilderClassname = ClassName.get(pkgName, typeElement.getSimpleName().toString() + BUILDER_CLASSNAME_POST_FIX);
      final ClassName invocationHandlerClassname = ClassName.get(pkgName, invocationHandlerBuilder.build().name);

      final TypeSpec.Builder adapterBuilderTypeSpecBuilder = createAdapterBuilderBuilder(typeElement, typeElementClassName, adapterBuilderClassname, invocationHandlerClassname);

      //TODO how to get Methods from the father??
      final List<? extends Element> enclosedElements = annotatedElement.getEnclosedElements();
      workEclosedElementsOnThisLevel(typeElement, pkgName, invocationHandlerBuilder, adapterBuilderClassname, adapterBuilderTypeSpecBuilder, enclosedElements);

      //write InvocationHandler
      writeDefinedClass(pkgName, invocationHandlerBuilder);
      //write Builder
      writeDefinedClass(pkgName, adapterBuilderTypeSpecBuilder);

    }
    return true;
  }

  public void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }

  private TypeSpec.Builder createTypedTypeSpecBuilder(TypeElement typeElement, Class class2Extend, String classnamePostFix) {
    // Get the full QualifiedTypeName
    final ClassName extendedInvocationHandlerClassName = ClassName.get(class2Extend);
    final ParameterizedTypeName typedExtendedInvocationHandler = ParameterizedTypeName.get(extendedInvocationHandlerClassName, TypeName.get(typeElement.asType()));

    return TypeSpec
        .classBuilder(typeElement.getSimpleName().toString() + classnamePostFix)
        .superclass(typedExtendedInvocationHandler)
        .addModifiers(Modifier.PUBLIC);
  }

  private TypeSpec.Builder createAdapterBuilderBuilder(TypeElement typeElement, ClassName typeElementClassName, ClassName adapterBuilderClassname, ClassName invocationHandlerClassname) {
    final TypeSpec.Builder adapterBuilderTypeSpecBuilder = createTypedTypeSpecBuilder(typeElement, AdapterBuilder.class, BUILDER_CLASSNAME_POST_FIX);

    final FieldSpec invocationHandler = FieldSpec
        .builder(invocationHandlerClassname, "invocationHandler")
        .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
        .initializer("new $T()", invocationHandlerClassname)
        .build();

    adapterBuilderTypeSpecBuilder
        //add static newBuilder method
        .addMethod(MethodSpec
            .methodBuilder("newBuilder")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .addStatement("return new $T()", adapterBuilderClassname)
            .returns(adapterBuilderClassname)
            .build())
        //add class attributre
        .addField(invocationHandler)
        //add getInvocationHandler
        .addMethod(MethodSpec
            .methodBuilder("getInvocationHandler")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addStatement(" return invocationHandler")
            .returns(invocationHandlerClassname)
            .build())
        //add setOriginal
        .addMethod(MethodSpec
            .methodBuilder("setOriginal")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ParameterSpec.builder(typeElementClassName, "original", Modifier.FINAL).build())
            .addStatement("invocationHandler.setOriginal(original)")
            .addStatement("return this")
            .returns(adapterBuilderClassname)
            .build()
        );
    return adapterBuilderTypeSpecBuilder;
  }

  private void workEclosedElementsOnThisLevel(final TypeElement typeElement, final String pkgName, final TypeSpec.Builder invocationHandlerBuilder,
                                              final ClassName adapterBuilderClassname, final TypeSpec.Builder adapterBuilderTypeSpecBuilder,
                                              final List<? extends Element> enclosedElements) {
    for (Element enclosed : enclosedElements) {
      if (enclosed.getKind() == ElementKind.METHOD) {
        ExecutableElement methodElement = (ExecutableElement) enclosed;
        if (methodElement.getModifiers().contains(Modifier.PUBLIC)) {
          final TypeMirror returnType = methodElement.getReturnType();

          final List<ParameterSpec> parameterSpecList = defineParamsForMethod(methodElement);

          final MethodSpec.Builder methodSpecBuilder = createMethodSpecBuilder(methodElement, returnType, parameterSpecList);

          final Optional<TypeSpec> functionalInterfaceSpec = writeFunctionalInterface(methodElement, methodSpecBuilder);
          addBuilderMethodForFunctionalInterface(pkgName, invocationHandlerBuilder, methodElement, functionalInterfaceSpec);
          //nun alle Delegator Methods

          final String methodSimpleName = methodElement.getSimpleName().toString();
          String methodimpleNameUpper = methodSimpleName.substring(0, 1).toUpperCase() + methodSimpleName.substring(1);

          final ClassName bestGuess = ClassName.get(pkgName, functionalInterfaceSpec.get().name);
          final ParameterSpec parameterSpec = ParameterSpec.builder(bestGuess, "adapter", Modifier.FINAL).build();

          adapterBuilderTypeSpecBuilder
              .addMethod(MethodSpec
                  .methodBuilder("with" + methodimpleNameUpper)
                  .addModifiers(Modifier.PUBLIC)
                  .addParameter(parameterSpec)
                  .addStatement("invocationHandler." + methodSimpleName + "(adapter)")
                  .addStatement("return this")
                  .returns(adapterBuilderClassname)
                  .build())
              .build();
        }
      }
    }
  }


  private MethodSpec.Builder createMethodSpecBuilder(ExecutableElement methodElement, TypeMirror returnType, List<ParameterSpec> parameterSpecs) {
    final MethodSpec.Builder methodSpecBuilder = MethodSpec
        .methodBuilder(methodElement.getSimpleName().toString())
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
        .returns(TypeName.get(returnType));

    parameterSpecs.forEach(methodSpecBuilder::addParameter);
    return methodSpecBuilder;
  }


  private void addBuilderMethodForFunctionalInterface(String pkgName, TypeSpec.Builder invocationHandlerBuilder, ExecutableElement methodElement, Optional<TypeSpec> functionalInterfaceSpec) {
    functionalInterfaceSpec.ifPresent(f -> {
      final TypeSpec funcInterfaceSpec = functionalInterfaceSpec.get();

      final ClassName bestGuess = ClassName.get(pkgName, funcInterfaceSpec.name);
      final ParameterSpec parameterSpec = ParameterSpec.builder(bestGuess, "adapter", Modifier.FINAL).build();

      final MethodSpec adapterMethodSpec = MethodSpec
          .methodBuilder(methodElement.getSimpleName().toString())
          .addModifiers(Modifier.PUBLIC)
          .returns(void.class)
          .addParameter(parameterSpec)
          .addCode(CodeBlock.builder().addStatement("addAdapter(adapter)").build())
          .build();
      invocationHandlerBuilder.addMethod(adapterMethodSpec);
    });
  }

}
