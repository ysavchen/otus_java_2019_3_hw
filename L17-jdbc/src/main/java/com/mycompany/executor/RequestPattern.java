package com.mycompany.executor;

import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class RequestPattern {

    private final Map<Class<?>, RequestDetails> requestDetails = new HashMap<>();

    private final RequestTypes requestType;

    private Object object;
    private Field[] fields;

    private String sqlRequest;

    RequestPattern(RequestTypes requestType) {
        this.requestType = requestType;
    }

    void analyze(Object object) {
        this.object = object;
        this.fields = object.getClass().getDeclaredFields();
        checkIdPresent();

        Class<?> clazz = object.getClass();
        RequestDetails details = requestDetails.get(clazz);
        if (details == null) {
            details = new RequestDetails(requestType, clazz);
            requestDetails.put(clazz, details);
        }

        generateSqlRequest(details);
    }

    private void generateSqlRequest(RequestDetails details) {
        switch (requestType) {
            case INSERT:
                this.sqlRequest = requestType.getType() + " into " +
                        details.getTable() + " (" +
                        details.getColumns() + ") values (" +
                        Stream.of(fields)
                                .map(field -> "?")
                                .collect(joining(", ")) + ")";
                break;

            case UPDATE:
                this.sqlRequest = requestType.getType() + " " + details.getTable() + " set " + details.getColumns();
                break;

            case LOAD:
                String idField = Stream.of(fields)
                        .filter(field -> {
                                    field.setAccessible(true);
                                    return field.getAnnotation(Id.class) != null;
                                }
                        ).findFirst()
                        .orElseThrow(() -> new NoIdFoundException("Entity does not have a field with @Id - " + object))
                        .getName();
                this.sqlRequest = requestType.getType() + " " + details.getColumns() + " from " + details.getTable() + " where " + idField + " = ?";
                break;

            default:
                throw new IllegalArgumentException("Invalid type: " + requestType);
        }
    }

    String getRequest() {

        return sqlRequest;
    }

    /**
     * Checks if an object has an exactly one field marked by @Id.
     */
    private void checkIdPresent() {
        int numIds = 0;
        for (var field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                numIds++;
            }
        }

        if (numIds == 0) {
            throw new NoIdFoundException("Entity does not have a field with @Id - " + object);
        }
        if (numIds > 1) {
            throw new SeveralIdsFoundException("Entity have several fields with @Id - " + object);
        }
    }
}
