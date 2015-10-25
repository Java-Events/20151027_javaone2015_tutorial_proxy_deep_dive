package org.rapidpm.event.javaone.chap05.usages.u001;


/**
 * Created by svenruppert on 24.10.15.
 */
public class ServiceImpl implements Service {
  @Override
  public String doWorkA(final String txt) {
    return this.getClass().getSimpleName() + ".doWorkA - " + txt;
  }

  @Override
  public String doWorkB(final String txt) {
    return this.getClass().getSimpleName() + ".doWorkB - " + txt;
  }
}
