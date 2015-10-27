package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version006;



import org.rapidpm.event.javaone.chap01.proxy.model.Service;
import org.rapidpm.event.javaone.chap01.proxy.model.ServiceImpl;

import static java.lang.System.out;
import static java.time.LocalDateTime.*;

/**
 * Created by svenruppert on 30.09.15.
 */
public class VirtualProxy implements Service {
  public VirtualProxy() { out.println("VirtualProxy " + now()); }
  private Service service = null;

  @Override
  public String work(String txt) {
    if(service == null){
      service = new ServiceImpl();
    }
    return service.work(txt);
  }
}