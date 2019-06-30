package com.mycompany.types;

import com.mycompany.base.TraversedField;
import com.mycompany.base.Visitor;

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
