package com.mycompany;

import com.mycompany.base.Visitor;
import com.mycompany.types.*;

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
            return "null";
        }
        JsonVisitor visitor = new JsonSerializationVisitor();
        new JsonSerializer().traverseObject(object, visitor);

        return visitor.getJsonValue().toString();
    }

    void traverseObject(Object object, Visitor visitor) {
        if (object.getClass() == String.class) {
            new TraversedString(null, object).accept(visitor);

        } else if (isPrimitiveWrapper(object.getClass())) {
            new TraversedPrimitiveWrapper(null, object).accept(visitor);

        } else if (object.getClass().isPrimitive()) {
            new TraversedPrimitive(null, object).accept(visitor);

        } else if (object.getClass().isArray() || object instanceof Collection<?>) {
            new TraversedArray(null, object).accept(visitor);

        } else {
            new TraversedObject(null, object).accept(visitor);

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

                    } else if (isPrimitiveWrapper(field.getType())) {
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

    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Byte.class ||
                clazz == Short.class ||
                clazz == Boolean.class ||
                clazz == Character.class;
    }
}
