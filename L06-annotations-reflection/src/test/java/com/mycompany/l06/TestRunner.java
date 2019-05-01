package com.mycompany.l06;

import com.mycompany.l06.tests.*;

public class TestRunner {

    public static void main(String[] args) {
        TestFrameworkCore.run(FirstTestSuite.class);
        TestFrameworkCore.run(SecondTestSuite.class);
        TestFrameworkCore.run(BeforeAllFailureSuite.class);
        TestFrameworkCore.run(BeforeEachFailureSuite.class);
        TestFrameworkCore.run(TestFailureSuite.class);
    }

}
