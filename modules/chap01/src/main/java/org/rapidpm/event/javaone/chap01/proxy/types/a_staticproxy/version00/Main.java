package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version00;


import org.rapidpm.event.javaone.chap01.proxy.model.Service;

/**
 * Created by Sven Ruppert on 22.09.2014.
 */
public class Main {
  public static void main(String[] args) {
    Service service = new ServiceProxy();
    System.out.println(service.work("Hallo"));
  }
}
