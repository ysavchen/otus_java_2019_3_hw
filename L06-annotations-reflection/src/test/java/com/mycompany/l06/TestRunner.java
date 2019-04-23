package com.mycompany.l06;

import com.mycompany.l06.tests.AnnotationsTest;

public class TestRunner {

    public static void main(String[] args) {
        TestFrameworkCore testFrameWork = new TestFrameworkCore();
        testFrameWork.run(AnnotationsTest.class);
    }

}
