package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class FirstTestSuite {

    FirstTestSuite() {
        System.out.println("Call of the constructor");
    }

    @BeforeAll
    static void beforeAllOne() {
        System.out.println("BeforeAllOne");
    }

    @BeforeAll
    static void beforeAllTwo() {
        System.out.println("BeforeAllTwo");
    }

    @BeforeEach
    void beforeEach1() {
        System.out.println("BeforeEach1");
    }

    @BeforeEach
    void beforeEach2() {
        System.out.println("BeforeEach2");
    }

    @Test
    void testOne() {
        System.out.println("testOne");
    }

    @Test
    void testTwo() {
        System.out.println("testTwo");
    }

    @AfterEach
    void afterEach1() {
        System.out.println("AfterEach1");
    }

    @AfterEach
    void afterEach2() {
        System.out.println("AfterEach2");
    }

    @AfterAll
    static void afterAllOne() {
        System.out.println("AfterAllOne");
    }

    @AfterAll
    static void afterAllTwo() {
        System.out.println("AfterAllTwo");
    }

}
