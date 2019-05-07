package com.mycompany.l08;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyCreator {

    /**
     * Creates a proxy with added logging behavior for methods marked with {@link Log}.
     *
     * @param clazz class to be proxied
     * @return proxied class
     */
    public static <T extends Logging> Logging newInstance(Class<T> clazz) {
        final List<Method> logMethods = new ArrayList<>();

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
        private final List<Method> logMethods;

        LogInvocationHandler(Logging obj, List<Method> logMethods) {
            this.obj = obj;
            this.logMethods = logMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.contains(method)) {
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i != 0) params.append(", ");
                    params.append(args[i].toString());
                }
                System.out.println("executed method: " + method.getName() + ", params: " + params);
            }

            return method.invoke(obj, args);
        }
    }
}
