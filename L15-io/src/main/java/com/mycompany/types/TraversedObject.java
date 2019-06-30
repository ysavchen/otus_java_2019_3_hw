package com.mycompany.types;

import com.mycompany.base.TraversedField;
import com.mycompany.base.Visitor;

import java.lang.reflect.Field;

public class TraversedObject extends TraversedField {

    private final Object object;

    public TraversedObject(Field field, Object object) {
        super(field);
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
