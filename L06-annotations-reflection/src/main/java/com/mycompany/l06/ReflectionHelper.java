package com.mycompany.l06;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ReflectionHelper {

    //private constructor, ensuring non-instantiability
    private ReflectionHelper() {
    }

    static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                final Constructor<T> constructor = type.getDeclaredConstructor();
                return constructor.newInstance();
            } else {
                final Class<?>[] classes = toClasses(args);
                final Constructor<T> constructor = type.getDeclaredConstructor(classes);
                return constructor.newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }
}
