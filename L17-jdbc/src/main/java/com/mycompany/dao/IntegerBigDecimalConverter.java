package com.mycompany.dao;

import java.math.BigDecimal;

public class IntegerBigDecimalConverter implements AttributeConverter<Integer, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Integer attribute) {
        if (attribute == null) {
            return null;
        }
        return BigDecimal.valueOf(attribute);
    }

    @Override
    public Integer convertToEntityAttribute(BigDecimal dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.intValue();
    }
}
