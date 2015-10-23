package org.rapidpm.event.javaone.chap03.usages.u001.v001;

import org.rapidpm.event.javaone.chap03.generator.annotations.Delegator;
import org.rapidpm.event.javaone.chap03.generator.annotations.VirtualProxy;
import org.rapidpm.event.javaone.chap03.generator.annotations.VirtualProxyNotThreadSave;

import java.time.LocalDateTime;

/**
 * Created by svenruppert on 20.10.15.
 */
@Delegator
@VirtualProxyNotThreadSave
@VirtualProxy
public interface Service {
  String doWork(String txt, LocalDateTime localDateTime);
}
