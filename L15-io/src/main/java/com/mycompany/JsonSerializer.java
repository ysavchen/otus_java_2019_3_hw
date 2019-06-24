package com.mycompany;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Cвой json object writer
 * Напишите свой json object writer (object to JSON string) аналогичный gson на основе javax.json.
 * <p>
 * Поддержите:
 * - массивы объектов и примитивных типов
 * - коллекции из стандартный библиотеки.
 * <p>
 * Take into consideration transient and static fields
 */
public class JsonSerializer {


    public static String toJson(Object object) throws IllegalAccessException {
        if (object == null) {
            return "{}";
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        new JsonSerializer().navigateTree(object, builder);
        JsonObject jsonCreated = builder.build();
        System.out.println("jsonCreated:" + jsonCreated);
        return jsonCreated.toString();
    }

    private void navigateTree(Object object, JsonObjectBuilder builder) throws IllegalAccessException {
        //array for primitives
        if (object.getClass().isArray()) {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                Object arrayElement = Array.get(object, i);
                System.out.println("Class: " + arrayElement.getClass());
                if (arrayElement.getClass() == int.class || arrayElement == Integer.class) {
                    jsonArrayBuilder.add(covertObject(arrayElement));
                }
                if (arrayElement.getClass() == String.class) {
                    jsonArrayBuilder.add(covertObject(arrayElement));
                }
//                JsonObjectBuilder arrObjectBuilder = Json.createObjectBuilder();
//                navigateTree(arrayElement, arrObjectBuilder);
            }
            builder.add(object.getClass().getName(), jsonArrayBuilder);
        } else {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (Modifier.isStatic(field.getModifiers())) {
                    //todo: add processing for static fields
                    continue;
                }
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                if (field.getType().isPrimitive()) {
                    objectBuilder.add(field.getName(), field.get(object).toString());
                }
                if (field.get(object) instanceof String) {
                    objectBuilder.add(field.getName(), field.get(object).toString());
                    continue;
                }
                if (field.get(object) instanceof Integer) {
                    objectBuilder.add(field.getName(), field.get(object).toString());
                    continue;
                }
                if (field.getType().isArray()) {
                    //todo: copypaste
                    JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                    int length = Array.getLength(field.get(object));
                    for (int i = 0; i < length; i++) {
                        Object arrayElement = Array.get(field.get(object), i);
                        System.out.println("Class: " + arrayElement.getClass());
                        if (arrayElement.getClass() == int.class || arrayElement == Integer.class) {
                            jsonArrayBuilder.add(covertObject(arrayElement));
                        }
                        if (arrayElement.getClass() == String.class) {
                            jsonArrayBuilder.add(covertObject(arrayElement));
                        }

//                        JsonObjectBuilder arrObjectBuilder = Json.createObjectBuilder();
//                        navigateTree(arrayElement, jsonArrayBuilder);
                    }
                    objectBuilder.add(field.getName(), jsonArrayBuilder);
                } else {
                    navigateTree(field.get(object), objectBuilder);
                }
            }

            builder.add(object.getClass().getName(), objectBuilder);
        }
    }

/*
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                //todo: add processing for static fields
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (field.getType().isPrimitive()) {
                builder.add(field.getName(), field.get(object).toString());
            }
            if (field.get(object) instanceof String) {
                builder.add(field.getName(), field.get(object).toString());
            }
            if (field.getType().isArray()) {
                //todo: copypaste
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                int length = Array.getLength(field.get(object));
                for (int i = 0; i < length; i++) {
                    Object arrayElement = Array.get(field.get(object), i);
                    jsonArrayBuilder.add(covertObject(arrayElement));
                }
                builder.add(field.getName(), jsonArrayBuilder);
            } else {
                //todo: object is Object
            }
        }*/


    /**
     * Returns a type that is functionally equal but not necessarily equal
     * according to {@link Object#equals(Object) Object.equals()}. The returned
     * type is {@link java.io.Serializable}.
     */
//    public static Type canonicalize(Type type) {
//        if (type instanceof Class) {
//            Class<?> c = (Class<?>) type;
//            return c.isArray() ? canonicalize(c.getComponentType()) : c;
//        }
//    }
    public JsonValue covertObject(Object object) {
        if (object.getClass() == Integer.class || object.getClass() == int.class) {
            return Json.createValue((Integer) object);
        }
        if (object.getClass() == String.class) {
            return Json.createValue((String) object);
        }

        return null;
    }
}
