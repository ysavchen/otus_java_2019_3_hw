package com.mycompany.with_visitor;

import com.mycompany.with_visitor.base.Visitor;
import com.mycompany.with_visitor.types.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;

public class JsonSerializationVisitor implements Visitor {

    private final JsonObjectBuilder builder;

    JsonSerializationVisitor(JsonObjectBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void visit(TraversedArray value) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        int length = Array.getLength(value.getArray());

        for (int i = 0; i < length; i++) {
            Object element = Array.get(value.getArray(), i);

            if (element.getClass() == String.class ||
                    element.getClass() == Integer.class ||
                    element.getClass() == int.class ||
                    element.getClass() == Long.class ||
                    element.getClass() == long.class ||
                    element.getClass() == Double.class ||
                    element.getClass() == double.class) {
                jsonArrayBuilder.add(convertObject(element));
            } else {
                JsonObjectBuilder innerObjectBuilder = Json.createObjectBuilder();
                Visitor visitor = new JsonSerializationVisitor(innerObjectBuilder);
                new JsonSerializer().traverseObject(element, visitor);
                jsonArrayBuilder.add(innerObjectBuilder);
            }
        }
        builder.add(value.getName(), jsonArrayBuilder);
    }

    @Override
    public void visit(TraversedPrimitive value) {
        builder.add(value.getName(), convertObject(value.getPrimitive()));
    }

    @Override
    public void visit(TraversedObject value) {
    }

    @Override
    public void visit(TraversedPrimitiveWrapper value) {
        builder.add(value.getName(), convertObject(value.getPrimitiveWrapper()));
    }

    @Override
    public void visit(TraversedString value) {
        builder.add(value.getName(), convertObject(value.getString()));
    }

    private JsonValue convertObject(Object object) {
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

        throw new IllegalArgumentException("Invalid type: " + object.getClass());
    }
}
