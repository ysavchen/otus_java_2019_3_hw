package com.mycompany;

import javax.json.*;
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
        new JsonSerializer().navigateTreesdf(object, builder);
        JsonObject jsonCreated = builder.build();
        System.out.println("jsonCreated:" + jsonCreated);
    }

    private void navigateTreesdf(Object object, JsonObjectBuilder builder) throws IllegalAccessException {
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
                    navigateTreesdf(array[i], newObjBuilder);
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

    private static void navigateTree(JsonValue tree) {
        switch (tree.getValueType()) {
            case OBJECT:
                System.out.println("OBJECT");
                JsonObject object = (JsonObject) tree;
                for (String name : object.keySet()) {
                    navigateTree(object.get(name));
                }
                break;
            case ARRAY:
                System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array) {
                    navigateTree(val);
                }
                break;
            case STRING:
                JsonString st = (JsonString) tree;
                System.out.println("STRING " + st.getString());
                break;
            case NUMBER:
                JsonNumber num = (JsonNumber) tree;
                System.out.println("NUMBER " + num.toString());
                break;
            case TRUE:
            case FALSE:
            case NULL:
                System.out.println(tree.getValueType().toString());
                break;
        }
    }
}
