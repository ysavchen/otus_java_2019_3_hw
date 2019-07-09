package com.mycompany.dao;

public interface AttributeConverter<X, Y> {

    Y convertToDatabaseColumn(X attribute);

    X convertToEntityAttribute(Y dbData);
}
