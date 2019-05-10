package com.mycompany.l08;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProxyCreator {

    /**
     * Creates a proxy with added logging behavior for methods marked with {@link Log}.
     *
     * @param clazz class to be proxied
     * @return proxied class
     */
    public static <T extends Logging> Logging newInstance(Class<T> clazz) {
        final Set<Method> logMethods = new HashSet<>();

        //collect methods declared in interface, which marked with @Log in a class
        for (Method clazzMethod : clazz.getMethods()) {
            if (clazzMethod.isAnnotationPresent(Log.class)) {
                for (Method interfaceMethod : Logging.class.getDeclaredMethods()) {
                    if (interfaceMethod.getName().equals(clazzMethod.getName())) {
                        logMethods.add(interfaceMethod);
                    }
                }
            }
        }

        final T obj = ReflectionUtils.instantiate(clazz);
        InvocationHandler handler = new LogInvocationHandler(obj, logMethods);
        return (Logging) Proxy.newProxyInstance(Logging.class.getClassLoader(),
                new Class<?>[]{Logging.class}, handler);
    }

    private static class LogInvocationHandler implements InvocationHandler {
        private final Logging obj;
        private final Set<Method> logMethods;

        LogInvocationHandler(Logging obj, Set<Method> logMethods) {
            this.obj = obj;
            this.logMethods = logMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.contains(method)) {
                String params = Stream.of(args)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                System.out.println("executed method: " + method.getName() + ", params: " + params);
            }

            return method.invoke(obj, args);
        }
    }
}
