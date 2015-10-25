package org.rapidpm.event.javaone.chap04.usages.u001;

import org.rapidpm.event.javaone.chap04.generator.annotations.dynamicobjectadapter.DynamicObjectAdapterBuilder;
import org.rapidpm.event.javaone.chap04.generator.annotations.staticobjectadapter.StaticObjectAdapter;

/**
 * Created by svenruppert on 24.10.15.
 */
@DynamicObjectAdapterBuilder
@StaticObjectAdapter
public interface Service {
  String doWorkA(String txt);
  String doWorkB(String txt);
}
