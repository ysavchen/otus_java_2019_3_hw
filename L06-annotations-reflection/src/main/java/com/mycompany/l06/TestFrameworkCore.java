package com.mycompany.l06;

import com.mycompany.l06.annotations.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TestFrameworkCore {

    public static void run(Class<?> testClass) {
        try {
            new Run(testClass).executeSuite();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Represents a new run for a test suite
     */
    private static class Run {
        private final List<Method> beforeAll = new ArrayList<>();
        private final List<Method> beforeEach = new ArrayList<>();
        private final List<Method> tests = new ArrayList<>();
        private final List<Method> afterEach = new ArrayList<>();
        private final List<Method> afterAll = new ArrayList<>();

        private final Class<?> testClass;

        private Object object;
        private boolean isAfterEachExecuted = false;

        Run(Class<?> testClass) {
            this.testClass = testClass;
        }

        private void executeSuite() {
            System.out.println("Executing suite: " + testClass.getName());
            dispatchMethods(testClass.getDeclaredMethods());

            try {
                beforeAll.forEach(method -> ReflectionUtils.callMethod(null, method));

                for (Method test : tests) {
                    try {
                        object = ReflectionUtils.instantiate(testClass);
                        beforeEach.forEach(method -> ReflectionUtils.callMethod(object, method));
                        ReflectionUtils.callMethod(object, test);
                    } catch (Exception ex) {
                        //must be executed if BeforeEach or Test is failed
                        executeAfterEach();
                    }
                    executeAfterEach();
                }
            } finally {
                //must be executed in any cases
                afterAll.forEach(method -> ReflectionUtils.callMethod(null, method));
            }
            System.out.println();
        }

        private void executeAfterEach() {
            if (object != null && !isAfterEachExecuted) {
                afterEach.forEach(method -> ReflectionUtils.callMethod(object, method));
            }
            isAfterEachExecuted = true;
        }

        private void dispatchMethods(Method[] methods) {
            for (Method method : methods) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(BeforeAll.class) && Modifier.isStatic(method.getModifiers())) {
                    beforeAll.add(method);
                }
                if (method.isAnnotationPresent(BeforeEach.class) && !Modifier.isStatic(method.getModifiers())) {
                    beforeEach.add(method);
                }
                if (method.isAnnotationPresent(Test.class) && !Modifier.isStatic(method.getModifiers())) {
                    tests.add(method);
                }
                if (method.isAnnotationPresent(AfterEach.class) && !Modifier.isStatic(method.getModifiers())) {
                    afterEach.add(method);
                }
                if (method.isAnnotationPresent(AfterAll.class) && Modifier.isStatic(method.getModifiers())) {
                    afterAll.add(method);
                }
                method.setAccessible(false);
            }
        }
    }
}
