package com.mycompany;

import com.mycompany.base.Visitor;
import com.mycompany.types.*;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * Cвой json object writer
 * Напишите свой json object writer (object to JSON string) аналогичный gson на основе javax.json.
 * <p>
 * Поддержите:
 * - массивы объектов и примитивных типов
 * - коллекции из стандартный библиотеки.
 */
public class JsonSerializer {

    public static String toJson(Object object) {
        if (object == null) {
            return "{}";
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Visitor visitor = new JsonSerializationVisitor(builder);
        new JsonSerializer().traverseObject(object, visitor);

        return builder.build().toString();
    }

    void traverseObject(Object object, Visitor visitor) {
        if (object.getClass().isArray() || object instanceof Collection<?>) {
            new TraversedArray(null, object).accept(visitor);
        } else {
            new TraversedObject(null, object).accept(visitor);
        }

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            try {
                if (field.getType() == String.class) {
                    new TraversedString(field, field.get(object)).accept(visitor);

                } else if (field.getType() == Integer.class ||
                        field.getType() == Long.class ||
                        field.getType() == Double.class) {
                    new TraversedPrimitiveWrapper(field, field.get(object)).accept(visitor);

                } else if (field.getType().isPrimitive()) {
                    new TraversedPrimitive(field, field.get(object)).accept(visitor);

                } else if (field.getType().isArray() || field.get(object) instanceof Collection<?>) {
                    new TraversedArray(field, field.get(object)).accept(visitor);

                } else {
                    traverseObject(field.get(object), visitor);
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
