package com.mycompany.l06;

import com.mycompany.l06.tests.AnnotationsTest;
import com.mycompany.l06.tests.SecondTestSuite;

public class TestRunner {

    public static void main(String[] args) {
        TestFrameworkCore testFrameWork = new TestFrameworkCore();
        testFrameWork.run(AnnotationsTest.class);
        testFrameWork.run(SecondTestSuite.class);
    }

}
