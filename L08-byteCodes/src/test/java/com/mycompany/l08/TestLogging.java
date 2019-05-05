package com.mycompany.l08;

public class TestLogging implements Logging {

    @Log
    @Override
    public void calculation(int param) {
    }

    @Log
    @Override
    public void testString(String str) {
    }

    @Log
    @Override
    public void testMyClass(MyClass myClass) {
    }

    @Log
    @Override
    public void testTwoParams(double paramOne, Boolean paramTwo) {
    }
}
