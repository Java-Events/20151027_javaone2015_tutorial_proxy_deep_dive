package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version01_01;


import org.rapidpm.event.javaone.chap01.proxy.model.Service;
import org.rapidpm.event.javaone.chap01.proxy.model.ServiceImpl;

/**
 * Created by sven on 13.04.15.
 */
public class ServiceStrategyFactoryNotThreadSave implements ServiceStrategyFactory {

    Service realSubject;

    @Override
    public Service realSubject(final ServiceFactory factory) {
        if (realSubject == null) {
            realSubject = factory.createInstance();
        }
        return realSubject;
    }
}
