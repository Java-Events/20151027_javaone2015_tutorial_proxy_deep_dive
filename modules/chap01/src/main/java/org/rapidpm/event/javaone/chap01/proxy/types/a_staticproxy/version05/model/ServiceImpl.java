package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.model;

import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 22.09.2014.
 */
public class ServiceImpl implements Service {
  public ServiceImpl() {
    System.out.println(" ServiceImpl = " + LocalDateTime.now());
  }

  @Override public String work(String txt) {
    return "ServiceImpl - " + txt;
  }
}
