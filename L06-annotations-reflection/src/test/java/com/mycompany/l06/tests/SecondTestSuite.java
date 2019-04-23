package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class SecondTestSuite {

    //must not be invoked as it's not static
    @BeforeAll
    void beforeAll() {
        System.out.println("BeforeAll in SecondTestSuite");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforeEach in SecondTestSuite");
    }

    @Test
    void test() {
        System.out.println("test in SecondTestSuite");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach in SecondTestSuite");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll in SecondTestSuite");
    }
}
