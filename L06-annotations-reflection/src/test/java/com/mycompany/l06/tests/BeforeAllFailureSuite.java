package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class BeforeAllFailureSuite {

    @BeforeAll
    static void beforeAll() {
        throw new RuntimeException();
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforeEach in BeforeAllFailureSuite");
    }

    @Test
    void test() {
        System.out.println("Test in BeforeAllFailureSuite");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach in BeforeAllFailureSuite");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll in BeforeAllFailureSuite");
    }
}
