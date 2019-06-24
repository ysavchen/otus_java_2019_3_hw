package com.mycompany.types;

import com.mycompany.base.TraversedField;
import com.mycompany.base.Visitor;

import java.lang.reflect.Field;

public class TraversedPrimitive extends TraversedField {

    private final Object primitive;

    public TraversedPrimitive(Field field, Object primitive) {
        super(field);
        this.primitive = primitive;
    }

    public Object getPrimitive() {
        return primitive;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
