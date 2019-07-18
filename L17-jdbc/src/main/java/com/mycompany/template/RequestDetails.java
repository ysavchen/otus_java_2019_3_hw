package com.mycompany.executor;

import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class RequestDetails {

    private final RequestTypes requestType;

    private final Field[] fields;

    private final String table;
    private String columns;
    private String sqlRequest;

    RequestDetails(RequestTypes requestType, Class<?> clazz) {
        this.requestType = requestType;
        this.table = clazz.getSimpleName();
        this.fields = clazz.getDeclaredFields();
        setColumns();
        generateSqlRequest(clazz);
    }

    String getRequest() {

        return sqlRequest;
    }

    Field[] getClassFields() {
        return fields;
    }

    private void setColumns() {
        switch (requestType) {
            case INSERT:
            case LOAD:
                this.columns = Stream.of(fields)
                        .map(Field::getName)
                        .collect(joining(", "));
                break;
            case UPDATE:
                StringBuilder columns = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i].getName();
                    if (i > 0) {
                        columns.append(", ");
                    }
                    columns.append(name).append(" = ?");
                }
                this.columns = columns.toString();
                break;
            default:
                throw new IllegalArgumentException("Invalid request type: " + requestType);
        }
    }

    private void generateSqlRequest(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        switch (requestType) {
            case INSERT:
                this.sqlRequest = requestType.getType() + " into " +
                        table +
                        " (" + columns + ") " +
                        "values (" +
                        Stream.of(fields)
                                .map(field -> "?")
                                .collect(joining(", ")) + ")";
                break;

            case UPDATE:
                this.sqlRequest = requestType.getType() + " " + table + " set " + columns;
                break;

            case LOAD:
                String idField = Stream.of(fields)
                        .filter(field -> {
                                    field.setAccessible(true);
                                    return field.getAnnotation(Id.class) != null;
                                }
                        ).findFirst()
                        .orElseThrow(() -> new NoIdFoundException("Entity does not have a field with @Id - " + clazz.getName()))
                        .getName();
                this.sqlRequest = requestType.getType() + " " + columns + " from " + table + " where " + idField + " = ?";
                break;

            default:
                throw new IllegalArgumentException("Invalid type: " + requestType);
        }
    }

}
