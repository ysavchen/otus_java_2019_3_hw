package com.mycompany.l08;

public class ProxyDemo {

    public static void main(String[] args) {
        Logging logging = ProxyCreator.create(TestLogging.class);
        logging.calculation(6);
        logging.testString("six");
        logging.testMyClass(new MyClass());
        logging.testTwoParams(25.64, true);
    }
}
