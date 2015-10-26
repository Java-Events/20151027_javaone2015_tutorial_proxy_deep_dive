package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version01_01;

import org.rapidpm.event.javaone.chap01.proxy.model.Service;
import org.rapidpm.event.javaone.chap01.proxy.model.ServiceImpl;

/**
 * Created by sven on 07.04.15.
 */
public interface ServiceFactory {
  Service createInstance();
}
