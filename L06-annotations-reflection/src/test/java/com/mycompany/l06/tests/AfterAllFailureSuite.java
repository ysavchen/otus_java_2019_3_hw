package com.mycompany.l06.tests;

import com.mycompany.l06.annotations.*;

public class AfterAllFailureSuite {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll in AfterAllFailureSuite");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BeforeEach in AfterAllFailureSuite");
    }

    @Test
    void test() {
        System.out.println("Test in AfterAllFailureSuite");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AfterEach inAfterAllFailureSuite");
    }

    @AfterAll
    static void afterAllOne() {
        throw new RuntimeException();
    }

    @AfterAll
    static void afterAllTwo() {
        System.out.println("AfterAllTwo in AfterAllFailureSuite");
    }
}
