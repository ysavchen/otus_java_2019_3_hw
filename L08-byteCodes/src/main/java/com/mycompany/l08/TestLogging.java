package com.mycompany.l08;

public class TestLogging implements Logging {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("calculation works");
    }

    @Log
    @Override
    public void testString(String str) {
        System.out.println("testString works");
    }

    @Log
    @Override
    public void testMyClass(MyClass myClass) {
        System.out.println("testMyClass works");
    }

    @Log
    @Override
    public void testTwoParams(double paramOne, Boolean paramTwo) {
        System.out.println("testTwoParams works");
    }

    @Override
    public void testNoAnnotation(int param){
        System.out.println("testNoAnnotation works");
    }
}
