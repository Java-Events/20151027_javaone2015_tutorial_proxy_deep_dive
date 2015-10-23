package org.rapidpm.event.javaone.chap03.generator.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.rapidpm.event.javaone.chap03.generator.annotations.Delegator;
import org.rapidpm.event.javaone.chap03.generator.annotations.IsDelegator;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by svenruppert on 20.10.15.
 */

@AutoService(Processor.class)
public class DelegatorAnnotationProcessor extends BasicDelegatorAnnotationProcessor<Delegator> {

  public Class<Delegator> responsibleFor() {
    return Delegator.class;
  }

  protected CodeBlock defineMethodImplementation(final ExecutableElement methodElement, final String methodName2Delegate) {
    final String delegateStatement = delegatorStatementWithReturn(methodElement, methodName2Delegate);

    return CodeBlock.builder()
        .addStatement(delegateStatement)
        .build();
  }

  @Override
  protected void addClassLevelSpecs(final TypeElement typeElement, final RoundEnvironment roundEnv) {
//additiona staff on class level, aditional methods and more
    final TypeName interface2Implement = TypeName.get(typeElement.asType());
    final TypeSpec.Builder specBuilderForTargetClass = createTypeSpecBuilderForTargetClass(typeElement, interface2Implement);


    specBuilderForTargetClass.addAnnotation(IsDelegator.class);

  }


}
