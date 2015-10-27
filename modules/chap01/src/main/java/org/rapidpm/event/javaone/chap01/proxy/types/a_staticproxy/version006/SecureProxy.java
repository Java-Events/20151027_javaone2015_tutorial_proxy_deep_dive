package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version006;


import org.rapidpm.event.javaone.chap01.proxy.model.Service;

import static java.lang.System.out;
import static java.time.LocalDateTime.now;

/**
 * Created by svenruppert on 30.09.15.
 */
public class SecureProxy implements Service {
  public SecureProxy() {
    out.println("SecureProxy " + now());
  }

  private Service service = new VirtualProxy();
  private String code = "";

  public void setCode(String code) {
    this.code = code;
  }

  public String work(String txt) {
    if (code.equals("hoppel")) {
      return service.work(txt);
    } else {
      return "nooooop";
    }
  }
}
