package org.rapidpm.event.javaone.chap04.usages.u001;

/**
 * Created by svenruppert on 25.10.15.
 */
public class Main001 {

  public static void main(String[] args) {
    Service service = new ServiceImpl();
    workOnService(service);

    System.out.println("= = = = = ");
    Service adapted = new ServiceStaticObjectAdapter()
        .withService(service)
        .withServiceMethodDoWorkA(txt -> "doWorkA-adapted");
    workOnService(adapted);

  }

  private static void workOnService(final Service service) {
    System.out.println("service.doWorkA(\"Hello\") = " + service.doWorkA("Hello"));
    System.out.println("service.doWorkB(\"Hello\") = " + service.doWorkB("Hello"));
  }
}
