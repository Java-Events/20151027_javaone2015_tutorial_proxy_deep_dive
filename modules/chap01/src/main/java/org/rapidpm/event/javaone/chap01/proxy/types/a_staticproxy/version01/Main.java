package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version01;


import org.rapidpm.event.javaone.chap01.proxy.model.Service;

/**
 * Created by Sven Ruppert on 15.09.2014.
 */
public class Main {
  public static void main(String[] args) {
    Service service = new VirtualServiceNotThreadSave();
    final String work = service.work("und los");
    System.out.println("work = " + work);
  }

}
