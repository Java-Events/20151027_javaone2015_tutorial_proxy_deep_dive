package org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.proxy;




import org.rapidpm.event.javaone.chap01.proxy.types.a_staticproxy.version05.model.Service;

import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 17.12.2014.
 */
public class VirtualProxy implements Service {

  public ServiceFactory serviceFactory;
  public ServiceStrategyFactory serviceStrategyFactory;

  private VirtualProxy(Builder builder) {
    System.out.println(" VirtualProxy = " + LocalDateTime.now());
    serviceFactory = builder.serviceFactory;
    setServiceStrategyFactory(builder.serviceStrategyFactory);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(VirtualProxy copy) {
    Builder builder = new Builder();
    builder.serviceFactory = copy.serviceFactory;
    builder.serviceStrategyFactory = copy.serviceStrategyFactory;
    return builder;
  }


  public String work(String txt) {
    return serviceStrategyFactory.realSubject(serviceFactory).work(txt);
  }

  public VirtualProxy setServiceStrategyFactory(ServiceStrategyFactory serviceStrategyFactory) {
    this.serviceStrategyFactory = serviceStrategyFactory;
    return this;
  }


  /**
   * Strategy of creating: ThreadSave, NotThreadSave, ...
   */
  public interface ServiceStrategyFactory{
    Service realSubject(ServiceFactory factory);
  }

  public interface ServiceFactory{
    Service createInstance();
  }


  public static final class Builder {
    private ServiceFactory serviceFactory;
    private ServiceStrategyFactory serviceStrategyFactory;

    private Builder() {
    }

    public Builder withServiceFactory(ServiceFactory serviceFactory) {
      this.serviceFactory = serviceFactory;
      return this;
    }

    public Builder withServiceStrategyFactory(ServiceStrategyFactory serviceStrategyFactory) {
      this.serviceStrategyFactory = serviceStrategyFactory;
      return this;
    }

    public VirtualProxy build() {
      return new VirtualProxy(this);
    }
  }
}
