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


    public static void toJson(Object object) throws IllegalAccessException {
        if (object == null) {
            // return "{}";
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        new JsonSerializer().navigateTree(object, builder);
        JsonObject jsonCreated = builder.build();
        System.out.println("jsonCreated:" + jsonCreated);
    }

    private void navigateTree(Object object, JsonObjectBuilder builder) throws IllegalAccessException {

        //array for primitives
        if (object.getClass().isArray()) {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                Object arrayElement = Array.get(object, i);
                jsonArrayBuilder.add(covertObject(arrayElement));
            }
            builder.add(object.getClass().getName(), jsonArrayBuilder);
        }


        if (object.getClass().isPrimitive()) {
            builder.add(object.getClass().getName(), object.toString());
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getType().isPrimitive()) {
                builder.add(field.getName(), field.get(object).toString());
            }
            if (field.getType().isArray()) {
                Object[] array = (Object[]) field.get(object);
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                JsonObjectBuilder newObjBuilder = Json.createObjectBuilder();
                for (int i = 0; i < array.length; i++) {
                    navigateTree(array[i], newObjBuilder);
                }
                builder.add(field.getName(), arrayBuilder.add(newObjBuilder));
                break;
            }
        }
        /*
        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                navigateTreesdf(array[i]);
            }
        }*/

        if (object instanceof String) {

        }
    }

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
        if (object instanceof Integer) {
            return Json.createValue((Integer) object);
        }
        return null;
    }
}
