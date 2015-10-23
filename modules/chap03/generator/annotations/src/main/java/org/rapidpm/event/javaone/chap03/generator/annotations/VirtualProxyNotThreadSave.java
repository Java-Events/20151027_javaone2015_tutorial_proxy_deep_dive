package org.rapidpm.event.javaone.chap03.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by svenruppert on 21.10.15.
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface VirtualProxyNotThreadSave {
  //define behavior
}
