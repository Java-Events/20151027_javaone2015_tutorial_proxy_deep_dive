package org.rapidpm.event.javaone.chap03.generator.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.rapidpm.event.javaone.chap03.generator.annotations.IsVirtualProxy;
import org.rapidpm.event.javaone.chap03.generator.annotations.VirtualProxy;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by svenruppert on 22.10.15.
 */
@AutoService(Processor.class)
public class VirtualProxyAnnotationProcessor extends BasicDelegatorAnnotationProcessor<VirtualProxy> {

  public static final String FACTORY = "Factory";
  public static final String FACTORY_CREATE_INSTANCE = "createInstance";
  public static final String STRATEGY_FACTORY = "StrategyFactory";
  public static final String STRATEGYFACTORY_REAL_SUBJECT = "realSubject";

  @Override
  public Class<VirtualProxy> responsibleFor() {
    return VirtualProxy.class;
  }

  protected CodeBlock defineMethodImplementation(final ExecutableElement methodElement, final String methodName2Delegate) {
    return CodeBlock.builder()
        .addStatement("return strategyFactory."+STRATEGYFACTORY_REAL_SUBJECT+"(factory)." + delegatorMethodCall(methodElement,methodName2Delegate))
        .build();
  }

  @Override
  protected void addClassLevelSpecs(final TypeElement typeElement, final RoundEnvironment roundEnv) {
//additiona staff on class level, aditional methods and more

    final TypeName interface2Implement = TypeName.get(typeElement.asType());
    final TypeSpec.Builder specBuilderForTargetClass = createTypeSpecBuilderForTargetClass(typeElement, interface2Implement);

    final ClassName implClassName = ClassName.get(pkgName(typeElement), className(typeElement)+ "Impl");
    final ClassName factoryClassName = ClassName.get(pkgName(typeElement), FACTORY);
    final ClassName strategyFactoryClassName = ClassName.get(pkgName(typeElement), STRATEGY_FACTORY);

    specBuilderForTargetClass.addAnnotation(IsVirtualProxy.class);

    final TypeSpec typeSpecFactory = TypeSpec.classBuilder(FACTORY)
        .addModifiers(PUBLIC, STATIC)
        .addMethod(MethodSpec.methodBuilder(FACTORY_CREATE_INSTANCE)
            .addModifiers(PUBLIC)
            .addCode(CodeBlock.builder()
                .addStatement("return new $T()", implClassName)
                .build())
            .returns(interface2Implement)
            .build())
        .build();
    specBuilderForTargetClass.addType(typeSpecFactory);

    final TypeSpec typeSpecStrategyFactory = TypeSpec.classBuilder(STRATEGY_FACTORY)
        .addModifiers(PUBLIC, STATIC)
        .addField(defineDelegatorField(typeElement))
        .addMethod(MethodSpec.methodBuilder(STRATEGYFACTORY_REAL_SUBJECT)
            .addModifiers(PUBLIC)
            .addParameter(ParameterSpec.builder(factoryClassName, "factory", FINAL).build())
             .addCode(CodeBlock.builder()
                 .beginControlFlow("if(delegator == null)")
                 .addStatement("delegator = factory."+FACTORY_CREATE_INSTANCE+"()")
                 .endControlFlow()
                 .addStatement("return delegator")
                 .build())
            .returns(interface2Implement)
            .build()
        ).build();

    specBuilderForTargetClass.addType(typeSpecStrategyFactory);


    specBuilderForTargetClass
        .addField(FieldSpec
            .builder(factoryClassName,"factory", Modifier.PRIVATE)
            .initializer("new $T()", factoryClassName)
            .build());

    specBuilderForTargetClass
        .addField(FieldSpec
            .builder(strategyFactoryClassName,"strategyFactory", Modifier.PRIVATE)
            .initializer("new $T()", strategyFactoryClassName)
            .build());

  }

}
