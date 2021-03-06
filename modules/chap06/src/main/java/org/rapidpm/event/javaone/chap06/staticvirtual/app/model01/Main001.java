package org.rapidpm.event.javaone.chap06.staticvirtual.app.model01;

import org.rapidpm.event.javaone.chap06.staticvirtual.Concurrency;
import org.rapidpm.event.javaone.chap06.staticvirtual.ProxyGenerator;

/**
 * Created by svenruppert on 23.10.15.
 */
public class Main001 {

  public static void main(String[] args) {

    final Service proxy01 = ProxyGenerator.make(Service.class, ServiceImpl.class, Concurrency.NO_DUPLICATES);
    final String result01 = proxy01.doWork("proxy01");

    final Service proxy02 = ProxyGenerator.make(Service.class, ServiceImpl.class, Concurrency.SOME_DUPLICATES);
    final String result02 = proxy01.doWork("proxy02");

  }
}
