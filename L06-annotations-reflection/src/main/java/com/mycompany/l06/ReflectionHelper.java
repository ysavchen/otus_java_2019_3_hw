package com.mycompany.l06;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionHelper {

    //private constructor, ensuring non-instantiability
    private ReflectionHelper() {
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        Constructor<T> constructor = null;
        try {
            if (args.length == 0) {
                constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } else {
                final Class<?>[] classes = toClasses(args);
                constructor = type.getDeclaredConstructor(classes);
                constructor.setAccessible(true);
                return constructor.newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }

        return null;
    }

    public static Object callMethod(Object object, String name, Object... args) {
        Method method = null;
        boolean isAccessible = true;
        try {
            method = object.getClass().getDeclaredMethod(name, toClasses(args));
            isAccessible = method.canAccess(object);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (method != null && !isAccessible) {
                method.setAccessible(false);
            }
        }
        return null;
    }

    public static Object callStaticMethod(Class<?> clazz, String name, Object... args) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.setAccessible(false);
            }
        }
        return null;
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }
}
