package com.mycompany.l08;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProxyCreator {

    public static <T extends Logging> Logging create(Class<T> clazz) {

        final List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(Log.class) != null) {
                methods.add(method);
            }
        }

        final T obj = ReflectionUtils.instantiate(clazz);
        if (!methods.isEmpty()) {
            InvocationHandler handler = new DemoInvocationHandler(obj);
            return (Logging) Proxy.newProxyInstance(ProxyCreator.class.getClassLoader(),
                    new Class<?>[]{Logging.class}, handler);
        }
        return null;
    }

    private static class DemoInvocationHandler implements InvocationHandler {
        private final Logging obj;

        DemoInvocationHandler(Logging obj) {
            this.obj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Type[] paramTypes = method.getGenericParameterTypes();
            for(Type type : paramTypes){
                System.out.println(type.getTypeName());
            }
            for(Object obj : args){
                System.out.println(obj.toString());
            }
            System.out.println("executed method: " + method.getName() + "params: " + args);
            return method.invoke(obj, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + obj +
                    '}';
        }
    }
}
