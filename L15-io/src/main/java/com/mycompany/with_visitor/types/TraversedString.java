package com.mycompany.with_visitor.types;

import com.mycompany.with_visitor.base.TraversedField;
import com.mycompany.with_visitor.base.Visitor;

import java.lang.reflect.Field;

public class TraversedString extends TraversedField {

    private final Object string;

    public TraversedString(Field field, Object string) {
        super(field);
        this.string = string;
    }

    public Object getString() {
        return string;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
