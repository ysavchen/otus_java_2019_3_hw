package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class BeforeEachFailureSuite {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll in BeforeEachFailureSuite");
    }

    @BeforeEach
    void beforeEach() {
        throw new RuntimeException();
    }

    @Test
    void test() {
        System.out.println("test in BeforeEachFailureSuite");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach in BeforeEachFailureSuite");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll in BeforeEachFailureSuite");
    }
}
