package com.mycompany.l011;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;

/**
 * <p>
 * Example for L01.1
 * <p>
 * To start the application:
 * mvn package
 * java -cp ./target/L01.1-maven.jar ru.otus.l011.HelloOtus
 * java -jar ./target/L01.1-maven.jar //java.lang.NoClassDefFoundError: com/google/common/collect/Lists
 * java -cp ./target/L01.1-maven.jar;{home_dir}\.m2\repository\com\google\guava\guava\23.0\guava-23.0.jar ru.otus.l011.HelloOtus
 * <p>
 * To unzip the jar:
 * 7z x -oJAR ./target/L01.1-maven.jar
 * unzip -d JAR ./target/L01.1-maven.jar
 * <p>
 * To build:
 * mvn package
 * mvn clean compile
 * mvn assembly:single
 * mvn clean compile assembly:single
 */
public class HelloOtus {

    public static void main(String... args) {

        Iterable<Integer> concatenated = Iterables.concat(
                Ints.asList(1, 2, 3),
                Ints.asList(4, 5, 6));
        System.out.println(concatenated);
    }
}
