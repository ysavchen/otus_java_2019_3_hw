package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedType;
import com.mycompany.with_visitor.base.Visitor;

public class TraversedPrimitive implements TraversedType {

    private final Object primitive;

    public TraversedPrimitive(Object primitive) {
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
