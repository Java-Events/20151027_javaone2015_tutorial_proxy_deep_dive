package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version01_01;

import org.rapidpm.event.javaone.chap01.proxy.model.Service;

/**
 * Created by svenruppert on 26.10.15.
 */
public class ServiceStrategyFactoryMethodScoped implements ServiceStrategyFactory {

  @Override
  public Service realSubject(final ServiceFactory factory) {
    return factory.createInstance();
  }
}