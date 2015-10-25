package org.rapidpm.event.javaone.chap05.generator.processor;

import com.google.auto.service.AutoService;
import org.rapidpm.event.javaone.chap05.generator.annotations.StaticObjectAdapter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by svenruppert on 24.10.15.
 */
@AutoService(Processor.class)
public class StaticObjactAdapterAnnotationProcessor extends AbstractProcessor {

  public Class<StaticObjectAdapter> responsibleFor() {
    return StaticObjectAdapter.class;
  }

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

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    //if responsible
    //if interface
    //splitt interface
    //



    return false;
  }
}
