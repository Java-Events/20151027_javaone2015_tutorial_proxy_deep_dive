package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version01_01;

import org.rapidpm.event.javaone.chap01.proxy.model.Service;
import org.rapidpm.event.javaone.chap01.proxy.model.ServiceImpl;

/**
 * Strategy of creating: ThreadSave, NotThreadSave, ...
 */
public interface ServiceStrategyFactory {
    Service realSubject(ServiceFactory factory);
}
