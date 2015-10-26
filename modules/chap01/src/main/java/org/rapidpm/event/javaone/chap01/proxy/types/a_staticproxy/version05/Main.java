package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05;


import org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.model.Service;
import org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.model.ServiceImpl;
import org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.proxy.SecureProxy;

import static org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.proxy.VirtualProxy.*;

/**
 * Created by Sven Ruppert on 17.12.2014.
 */
public class Main {
  public static void main(String[] args) throws InterruptedException {
//    checkSecureProxyOnly();


    final ServiceStrategyFactory strategyFactory = new ServiceStrategyFactory() {
      Service service = null; //nix lambda
      @Override
      public Service realSubject(ServiceFactory factory) {
        if (service == null) {
          service = factory.createInstance();
        }
        return service;
      }
    };


    final Builder virtualProxyBuilder = newBuilder()
        .withServiceFactory(ServiceImpl::new)
//        .withServiceFactory(aBuilder::build)
        .withServiceStrategyFactory(strategyFactory);

    final SecureProxy.Builder aBuilder = SecureProxy.newBuilder()
        .withCode("ccc")
//        .withService(secureProxy);
        .withService(virtualProxyBuilder.build());

    final SecureProxy proxy = aBuilder.build();

    Thread.sleep(5000);
    System.out.println(proxy.work("AA").equals("ServiceImpl - AA"));
    proxy.setCode("hoppel");
    System.out.println(proxy.work("AA").equals("ServiceImpl - AA"));



    final Service secureProxy = SecureProxy.newBuilder()
        .withCode("hoppel")
        .withService(virtualProxyBuilder.build())
        .build();

    System.out.println(secureProxy.work("AA").equals("ServiceImpl - AA"));




  }

  private static void checkSecureProxyOnly() {
    //erzeuge einen SecureProxy
    final SecureProxy secureProxy = SecureProxy.newBuilder()
        .withCode("ABC")
        .withService(new ServiceImpl())
        .build();
    System.out.println(secureProxy.work("AA").equals("nooooop"));

    secureProxy.setCode("hoppel");
    System.out.println(secureProxy.work("AA").equals("ServiceImpl - AA"));
  }

}
