package org.rapidpm.event.javaone.chap03.usages.u001.v001;

import java.time.LocalDateTime;

/**
 * Created by svenruppert on 22.10.15.
 */
public class ServiceImpl implements Service {
  @Override
  public String doWork(final String txt, final LocalDateTime localDateTime) {
    return "result - " + txt + " - " + localDateTime;
  }
}
