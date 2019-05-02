package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class AfterEachFailureSuite {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll in AfterEachFailureSuite");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforeEach in AfterEachFailureSuite");
    }

    @Test
    void test() {
        System.out.println("Test in AfterEachFailureSuite");
    }

    @AfterEach
    void afterEachOne() {
        throw new RuntimeException();
    }

    @AfterEach
    void afterEachTwo() {
        System.out.println("AfterEachTwo in AfterEachFailureSuite");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll in AfterEachFailureSuite");
    }
}
