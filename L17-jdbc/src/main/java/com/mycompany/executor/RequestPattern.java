package com.mycompany.executor;

import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class RequestPattern {

    private final Map<Class<?>, RequestDetails> requestDetails = new HashMap<>();

    private final RequestTypes requestType;

    RequestPattern(RequestTypes requestType) {
        this.requestType = requestType;
    }

    String analyze(Object object) {
        Class<?> clazz = object.getClass();
        checkIdPresent(clazz);

        RequestDetails details = requestDetails.get(clazz);
        if (details != null) {
            return details.getRequest();
        }

        details = new RequestDetails(requestType, clazz);
        requestDetails.put(clazz, details);
        return details.getRequest();
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
