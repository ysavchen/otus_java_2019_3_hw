package com.mycompany;

import com.mycompany.types.*;

import javax.json.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.function.Consumer;

public class JsonSerializationVisitor implements JsonVisitor {

    private final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    private JsonArrayBuilder arrayBuilder;
    private JsonValue jsonValue;

    @Override
    public void visit(TraversedArray value) {
        JsonArrayBuilder innerArrayBuilder = Json.createArrayBuilder();
        Consumer<Object> consumer = (element) -> {
            JsonValue jsonValue = toJsonValue(element);
            if (jsonValue != null) {
                innerArrayBuilder.add(jsonValue);
            } else {
                JsonVisitor jsonVisitor = new JsonSerializationVisitor();
                new JsonSerializer().traverseObject(element, jsonVisitor);
                innerArrayBuilder.add(jsonVisitor.getJsonValue());
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

        if ("null".equals(value.getName())) {
            this.arrayBuilder = innerArrayBuilder;
        } else {
            objectBuilder.add(value.getName(), innerArrayBuilder);
        }
    }

    @Override
    public void visit(TraversedPrimitive value) {
        if ("null".equals(value.getName())) {
            jsonValue = toJsonValue(value.getPrimitive());
        } else {
            objectBuilder.add(value.getName(), toJsonValue(value.getPrimitive()));
        }
    }

    @Override
    public void visit(TraversedObject value) {
        //must be empty
    }

    @Override
    public void visit(TraversedPrimitiveWrapper value) {
        if ("null".equals(value.getName())) {
            jsonValue = toJsonValue(value.getPrimitiveWrapper());
        } else {
            objectBuilder.add(value.getName(), toJsonValue(value.getPrimitiveWrapper()));
        }
    }

    @Override
    public void visit(TraversedString value) {
        if ("null".equals(value.getName())) {
            jsonValue = toJsonValue(value.getString());
        } else {
            objectBuilder.add(value.getName(), toJsonValue(value.getString()));
        }
    }

    /**
     * Converts an object to json value.
     *
     * @param object
     * @return JsonValue if an object is convertable, otherwise {@code null}
     */
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
        if (object.getClass() == Character.class || object.getClass() == char.class) {
            return Json.createValue(String.valueOf(object));
        }
        return null;
    }

    @Override
    public JsonValue getJsonValue() {
        JsonValue resultValue;
        if (jsonValue != null) {
            resultValue = jsonValue;
        } else if (arrayBuilder != null) {
            resultValue = arrayBuilder.build();
        } else {
            resultValue = objectBuilder.build();
        }
        return resultValue;
    }
}
