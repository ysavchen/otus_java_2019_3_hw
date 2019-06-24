package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedField;
import com.mycompany.with_visitor.base.Visitor;

import java.lang.reflect.Field;

public class TraversedArray extends TraversedField {

    private final Object array;

    public TraversedArray(Field field, Object array) {
        super(field);
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
