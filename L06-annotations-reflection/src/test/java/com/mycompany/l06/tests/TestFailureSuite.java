package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class TestFailureSuite {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll in TestFailureSuite");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforeEach in TestFailureSuite");
    }

    @Test
    void test() {
        throw new RuntimeException();
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach in TestFailureSuite");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll in TestFailureSuite");
    }
}
