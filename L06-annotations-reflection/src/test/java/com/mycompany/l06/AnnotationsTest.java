package com.mycompany.l06;

public class AnnotationsTest {

    AnnotationsTest() {
        System.out.println("Call of the constructor");
    }

    static void beforeAllOne() {
        System.out.println("BeforeAllOne");
    }

    static void beforeAllTwo() {
        System.out.println("BeforeAllTwo");
    }

    void beforeEach1() {
        System.out.println("BeforeEach1");
    }

    void beforeEach2() {
        System.out.println("BeforeEach2");
    }

    void testOne() {
        System.out.println("testOne");
    }

    void testTwo() {
        System.out.println("testTwo");
    }

    void afterEach1() {
        System.out.println("AfterEach1");
    }

    void afterEach2() {
        System.out.println("AfterEach2");
    }

    static void afterAllOne() {
        System.out.println("AfterAllOne");
    }

    static void afterAllTwo() {
        System.out.println("AfterAllTwo");
    }

}
