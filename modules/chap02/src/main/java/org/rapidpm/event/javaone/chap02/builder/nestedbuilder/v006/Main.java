package org.rapidpm.event.javaone.chap02.builder.nestedbuilder.v006;


/**
 * Created by sven on 05.03.15.
 */
public class Main {
  public static void main(String[] args) {
    Parent build = Parent.newBuilder()
        .addKidA().withNote("A")
                  .addKidB().withNote("B").done()
        .done()
        .build();
    System.out.println("build = " + build);
  }
}
