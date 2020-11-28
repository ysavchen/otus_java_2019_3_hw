package com.mycompany.template;

import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class ClassMetaData {

    private final Map<Class<?>, RequestDetails> classMetaData = new HashMap<>();

    private final RequestTypes requestType;

    ClassMetaData(RequestTypes requestType) {
        this.requestType = requestType;
    }

    RequestDetails analyze(Object object) {
        Class<?> clazz = object.getClass();

        RequestDetails details = classMetaData.get(clazz);
        if (details != null) {
            return details;
        }

        checkIdPresent(clazz);
        details = new RequestDetails(requestType, clazz);
        classMetaData.put(clazz, details);
        return details;
    }

    /**
     * Checks if an object has an exactly one field marked by @Id.
     */
    private void checkIdPresent(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        int numIds = 0;
        for (var field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                numIds++;
            }
        }

        if (numIds == 0) {
            throw new NoIdFoundException("Entity does not have a field with @Id - " + clazz.getName());
        }
        if (numIds > 1) {
            throw new SeveralIdsFoundException("Entity have several fields with @Id - " + clazz.getName());
        }
    }
}
