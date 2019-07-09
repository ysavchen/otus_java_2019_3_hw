package com.mycompany;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectionUtils {

    //private constructor, ensuring non-instantiability
    private ReflectionUtils() {
    }

    /**
     * Instantiates an object of the needed class.
     *
     * @param type class of object to be instantiated
     * @param args params for constructor
     * @return instantiated object of T for success, otherwise throws an exception
     */
    public static <T> T instantiate(Class<T> type, Object... args) {
        Constructor<T> constructor;
        try {
            if (args.length == 0) {
                constructor = type.getDeclaredConstructor();
                return makeAccessible(constructor, null).newInstance();
            } else {
                final Class<?>[] classes = toClasses(args);
                constructor = type.getDeclaredConstructor(classes);
                return makeAccessible(constructor, null).newInstance();
            }
        } catch (Throwable t) {
            throw throwAsUncheckedException(t);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException throwAsUncheckedException(Throwable t) throws T {
        throw (T) t;
    }

    /**
     * Makes a method/constructor accessible
     * <p>
     *
     * @param obj    method/constructor
     * @param target object where the method is invoked, or {@code null} for constructor or static method
     */
    private static <T extends AccessibleObject> T makeAccessible(T obj, Object target) {
        if (!obj.canAccess(target)) {
            obj.setAccessible(true);
        }
        return obj;
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }
}
