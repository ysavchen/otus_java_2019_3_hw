package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedField;
import com.mycompany.with_visitor.base.Visitor;

import java.lang.reflect.Field;

public class TraversedPrimitiveWrapper extends TraversedField {

    private final Object primitiveWrapper;

    public TraversedPrimitiveWrapper(Field field, Object primitiveWrapper) {
        super(field);
        this.primitiveWrapper = primitiveWrapper;
    }

    public Object getPrimitiveWrapper() {
        return primitiveWrapper;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
