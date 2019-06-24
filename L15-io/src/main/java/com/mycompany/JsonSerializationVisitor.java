package com.mycompany;

import com.mycompany.base.Visitor;
import com.mycompany.types.*;

import javax.json.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.function.Consumer;

public class JsonSerializationVisitor implements Visitor {

    private final JsonObjectBuilder objectBuilder;

    JsonSerializationVisitor(JsonObjectBuilder objectBuilder) {
        this.objectBuilder = objectBuilder;
    }

    @Override
    public void visit(TraversedArray value) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        Consumer<Object> consumer = (element) -> {
            if (element.getClass() == String.class ||
                    element.getClass() == Integer.class ||
                    element.getClass() == Long.class ||
                    element.getClass() == Double.class ||
                    element.getClass() == Short.class ||
                    element.getClass() == Float.class ||
                    element.getClass() == Boolean.class ||
                    element.getClass() == Byte.class ||
                    element.getClass().isPrimitive()) {
                jsonArrayBuilder.add(toJsonValue(element));
            } else {
                JsonObjectBuilder innerObjectBuilder = Json.createObjectBuilder();
                Visitor visitor = new JsonSerializationVisitor(innerObjectBuilder);
                new JsonSerializer().traverseObject(element, visitor);
                jsonArrayBuilder.add(innerObjectBuilder);
            }
        };

        if (value.getArray() instanceof Collection<?>) {
            ((Collection<?>) value.getArray()).forEach(consumer);
        } else {
            int length = Array.getLength(value.getArray());
            for (int i = 0; i < length; i++) {
                consumer.accept(Array.get(value.getArray(), i));
            }
        }

        objectBuilder.add(value.getName(), jsonArrayBuilder);
    }

    @Override
    public void visit(TraversedPrimitive value) {
        objectBuilder.add(value.getName(), toJsonValue(value.getPrimitive()));
    }

    @Override
    public void visit(TraversedObject value) {
        //must be empty
    }

    @Override
    public void visit(TraversedPrimitiveWrapper value) {
        objectBuilder.add(value.getName(), toJsonValue(value.getPrimitiveWrapper()));
    }

    @Override
    public void visit(TraversedString value) {
        objectBuilder.add(value.getName(), toJsonValue(value.getString()));
    }

    private JsonValue toJsonValue(Object object) {
        if (object.getClass() == String.class) {
            return Json.createValue((String) object);
        }
        if (object.getClass() == Integer.class || object.getClass() == int.class) {
            return Json.createValue((Integer) object);
        }
        if (object.getClass() == Long.class || object.getClass() == long.class) {
            return Json.createValue((Long) object);
        }
        if (object.getClass() == Double.class || object.getClass() == double.class) {
            return Json.createValue((Double) object);
        }
        if (object.getClass() == Float.class || object.getClass() == float.class) {
            return Json.createValue(((Float) object).doubleValue());
        }
        if (object.getClass() == Byte.class || object.getClass() == byte.class) {
            return Json.createValue(((Byte) object).intValue());
        }
        if (object.getClass() == Short.class || object.getClass() == short.class) {
            return Json.createValue(((Short) object).intValue());
        }
        if (object.getClass() == Boolean.class || object.getClass() == boolean.class) {
            return ((Boolean) object) ? JsonObject.TRUE : JsonObject.FALSE;
        }

        throw new IllegalArgumentException("Invalid type: " + object.getClass());
    }
}
