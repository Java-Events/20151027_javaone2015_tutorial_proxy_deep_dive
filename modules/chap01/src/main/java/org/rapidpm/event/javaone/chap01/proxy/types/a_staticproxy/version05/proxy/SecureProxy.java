package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.proxy;


import org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.model.Service;

import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 17.12.2014.
 */
public class SecureProxy implements Service {

  private Service service ;

  private SecureProxy(Builder builder) {
    System.out.println(" SecureProxy = " + LocalDateTime.now());
    setService(builder.service);
    setCode(builder.code);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(SecureProxy copy) {
    Builder builder = new Builder();
    builder.service = copy.service;
    builder.code = copy.code;
    return builder;
  }

  public void setService(Service service) {
    this.service = service;
  }

  private String code = "";

  //Simmulation der Tastatureingabe
  public void setCode(String code) {
    this.code = code;
  }

  public String work(String txt) {
    if(code.equals("hoppel")){
      return service.work(txt);
    } else{
      return "nooooop";
    }
  }


  public static final class Builder {
    private Service service;
    private String code;

    private Builder() {
    }

    public Builder withService(Service service) {
      this.service = service;
      return this;
    }

    public Builder withCode(String code) {
      this.code = code;
      return this;
    }

    public SecureProxy build() {
      return new SecureProxy(this);
    }
  }
}
