package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedField;
import com.mycompany.with_visitor.base.Visitor;

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
