package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedType;
import com.mycompany.with_visitor.base.Visitor;

public class TraversedObject implements TraversedType {

    private final Object object;

    public TraversedObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
