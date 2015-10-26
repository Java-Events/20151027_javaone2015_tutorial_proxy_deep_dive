package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version02;

import org.rapidpm.event.javaone.chap01.proxy.model.Service;
import org.rapidpm.event.javaone.chap01.proxy.model.ServiceImpl;

/**
 * Created by Sven Ruppert on 22.09.2014.
 */
public class Main {
  public static void main(String[] args) {
    Service proxy = new ServiceSecurityProxy();

    ((ServiceSecurityProxy)proxy).setCode("Nase");  //Eingabe simulieren
    System.out.println(proxy.work("Hallo"));

    ((ServiceSecurityProxy)proxy).setCode("hoppel"); //Eingabe simulieren
    System.out.println(proxy.work("Hallo"));
  }
}
