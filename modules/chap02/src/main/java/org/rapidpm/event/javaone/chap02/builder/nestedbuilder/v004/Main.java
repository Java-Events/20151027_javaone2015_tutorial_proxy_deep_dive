package org.rapidpm.event.javaone.chap02.builder.nestedbuilder.v004;

/**
 * Created by sven on 05.03.15.
 */
public class Main {
    public static void main(String[] args) {


      Car car = Car.newBuilder()
          .addEngine().withPower(100).withType(5).done()
          .addWheels()
            .addWheel().withType(1).withSize(2).withColour(2).addWheelToList()
            .addWheel().withType(1).withSize(2).withColour(2).addWheelToList()
            .addWheel().withType(1).withSize(2).withColour(2).addWheelToList()
          .done()
          .build();

        System.out.println("car = " + car);
    }
}
