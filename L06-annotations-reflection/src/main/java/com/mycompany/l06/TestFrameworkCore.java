package com.mycompany.l06;

import com.mycompany.l06.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFrameworkCore {

    private List<Method> beforeAll = new ArrayList<>();
    private List<Method> beforeEach = new ArrayList<>();
    private List<Method> tests = new ArrayList<>();
    private List<Method> afterEach = new ArrayList<>();
    private List<Method> afterAll = new ArrayList<>();

    public void run(Class<?> testClass) {
        dispatchMethods(testClass.getDeclaredMethods());

        beforeAll.forEach(method -> ReflectionHelper.callStaticMethod(testClass, method.getName()));

        tests.forEach(test -> {
            Object object = ReflectionHelper.instantiate(testClass);
            if (object != null) {
                beforeEach.forEach(method ->
                        ReflectionHelper.callMethod(object, method.getName())
                );

                ReflectionHelper.callMethod(object, test.getName());

                afterEach.forEach(method ->
                        ReflectionHelper.callMethod(object, method.getName())
                );
            }
        });

        afterAll.forEach(method -> ReflectionHelper.callStaticMethod(testClass, method.getName()));
    }

    private void dispatchMethods(Method[] methods) {
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(BeforeAll.class)) {
                beforeAll.add(method);
            }
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforeEach.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                tests.add(method);
            }
            if (method.isAnnotationPresent(AfterEach.class)) {
                afterEach.add(method);
            }
            if (method.isAnnotationPresent(AfterAll.class)) {
                afterAll.add(method);
            }
            method.setAccessible(false);
        }
    }
}
