package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedType;
import com.mycompany.with_visitor.base.Visitor;

public class TraversedArray implements TraversedType {

    private final Object array;

    public TraversedArray(Object array) {
        this.array = array;
    }

    public Object getArray() {
        return array;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
