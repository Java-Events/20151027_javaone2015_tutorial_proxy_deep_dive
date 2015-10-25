package org.rapidpm.event.javaone.chap03.generator.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.rapidpm.event.javaone.chap03.generator.annotations.IsVirtualProxy;
import org.rapidpm.event.javaone.chap03.generator.annotations.VirtualProxyNotThreadSave;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by svenruppert on 21.10.15.
 */
@AutoService(Processor.class)
public class VirtualProxyNotThreadSaveAnnotationProcessor extends BasicAnnotationProcessor<VirtualProxyNotThreadSave> {

  @Override
  public Class<VirtualProxyNotThreadSave> responsibleFor() {
    return VirtualProxyNotThreadSave.class;
  }

  protected CodeBlock defineMethodImplementation(final ExecutableElement methodElement, final String methodName2Delegate) {
    final String delegateStatement = delegatorStatementWithReturn(methodElement, methodName2Delegate);

    ClassName implClassName = ClassName.get(
        pkgName((TypeElement) methodElement.getEnclosingElement()),
        className(methodElement.getEnclosingElement()) + "Impl");

    return CodeBlock.builder()
        .beginControlFlow("if(delegator == null)")
          .addStatement("delegator = new $T()", implClassName)
        .endControlFlow()
        .addStatement(delegateStatement)
        .build();
  }

  @Override
  protected void addClassLevelSpecs(final TypeElement typeElement, final RoundEnvironment roundEnv) {
//additiona staff on class level, aditional methods and more
    final TypeName interface2Implement = TypeName.get(typeElement.asType());
    final TypeSpec.Builder specBuilderForTargetClass = createTypeSpecBuilderForTargetClass(typeElement, interface2Implement);

    specBuilderForTargetClass.addAnnotation(IsVirtualProxy.class);

    final FieldSpec delegatorFieldSpec = defineDelegatorField(typeElement);
    specBuilderForTargetClass.addField(delegatorFieldSpec);


  }

}