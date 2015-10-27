package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version006;

/**
 * Created by svenruppert on 30.09.15.
 */
public class Main {


  public static void main(String[] args) {

    final SecureProxy secureProxy = new SecureProxy();

    secureProxy.setCode("ABC");
    System.out.println("secureProxy = " + secureProxy.work("Hello"));
    secureProxy.setCode("hoppel");
    System.out.println("secureProxy = " + secureProxy.work("Hello"));



  }
}
