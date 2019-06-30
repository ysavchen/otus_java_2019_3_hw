package com.mycompany.base;

import java.lang.reflect.Field;

public abstract class TraversedField implements TraversedType {

    /**
     * Field of the object.
     * Root object is not assigned to any other object. So, field = null.
     */
    private final Field field;

    protected TraversedField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return field == null ? "null" : field.getName();
    }
}
