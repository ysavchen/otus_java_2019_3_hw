package com.mycompany.with_visitor;

import com.mycompany.with_visitor.base.Visitor;
import com.mycompany.with_visitor.types.TraversedArray;
import com.mycompany.with_visitor.types.TraversedObject;
import com.mycompany.with_visitor.types.TraversedPrimitive;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JsonSerializer {

    public static String toJson(Object object) throws IllegalAccessException {
        if (object == null) {
            return "{}";
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Visitor visitor = new JsonSerializationVisitor(builder);
        new JsonSerializer().traverseObject(object, visitor);

        JsonObject jsonCreated = builder.build();
        System.out.println("jsonCreated:" + jsonCreated);
        return jsonCreated.toString();
    }

    private void traverseObject(Object object, Visitor visitor) throws IllegalAccessException {
        if (object.getClass().isArray()) {
            new TraversedArray(object).accept(visitor);
        } else {
            new TraversedObject(object).accept(visitor);
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
            if (field.getType().isPrimitive()) {
                new TraversedPrimitive(field).accept(visitor);
            } else if (field.getType().isArray()) {
                new TraversedArray(field.get(object)).accept(visitor);
            } else {
                traverseObject(field.get(object), visitor);
            }
        }
    }
}
