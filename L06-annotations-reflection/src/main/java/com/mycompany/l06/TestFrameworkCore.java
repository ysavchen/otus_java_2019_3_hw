package com.mycompany.l06;

import com.mycompany.l06.annotations.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TestFrameworkCore {

    private List<Method> beforeAll;
    private List<Method> beforeEach;
    private List<Method> tests;
    private List<Method> afterEach;
    private List<Method> afterAll;

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
        beforeAll = new ArrayList<>();
        beforeEach = new ArrayList<>();
        tests = new ArrayList<>();
        afterEach = new ArrayList<>();
        afterAll = new ArrayList<>();

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
