package org.rapidpm.event.javaone.chap04.usages.u001.v001;


import org.rapidpm.event.javaone.chap04.usages.u001.Service;
import org.rapidpm.event.javaone.chap04.usages.u001.ServiceImpl;

/**
 * Created by svenruppert on 24.10.15.
 */
public class Main01 {
  public static void main(String[] args) {
    Service service = new ServiceImpl();
    workOnService(service);

    ServiceStaticObjectAdapter adapted = new ServiceStaticObjectAdapter();
    adapted.addService(service);
    adapted.addAdapterForDoWorkA(txt -> "doWorkA-adapted");
    workOnService(adapted);

    //split interface
    //

  }

  private static void workOnService(final Service service) {
    System.out.println("service.doWorkA(\"Hello\") = " + service.doWorkA("Hello"));
    System.out.println("service.doWorkB(\"Hello\") = " + service.doWorkB("Hello"));
  }

  //manual static ObjectAdapter
  public static class ServiceStaticObjectAdapter implements Service {

    private Service service;

    private Service_DoWorkA_Adapter doWorkAAdapter;
    private Service_DoWorkB_Adapter doWorkBAdapter;

    public void addService(Service service){ this.service = service; }
    public void addAdapterForDoWorkA(Service_DoWorkA_Adapter adapter){
      this.doWorkAAdapter = adapter;
    }
    public void addAdapterForDoWorkB(Service_DoWorkB_Adapter adapter){
      this.doWorkBAdapter = adapter;
    }

    public interface Service_DoWorkA_Adapter{ String doWorkA(final String txt); }
    public interface Service_DoWorkB_Adapter{ String doWorkB(final String txt); }

    @Override
    public String doWorkA(final String txt) {
      if (this.doWorkAAdapter != null) return this.doWorkAAdapter.doWorkA(txt);
      return service.doWorkA(txt);
    }

    @Override
    public String doWorkB(final String txt) {
      if (this.doWorkBAdapter != null) return this.doWorkBAdapter.doWorkB(txt);
      return service.doWorkB(txt);
    }
  }




}
