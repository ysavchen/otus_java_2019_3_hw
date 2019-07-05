package com.mycompany.executor;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class RequestDetails {

    private final RequestTypes type;

    private final String table;

    private String columns;

    RequestDetails(RequestTypes type, Class<?> clazz) {
        this.type = type;
        this.table = clazz.getSimpleName();
        setColumns(clazz.getDeclaredFields());
    }

    private void setColumns(Field[] fields) {
        switch (type) {
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
                throw new IllegalArgumentException("Invalid request type: " + type);
        }
    }

    String getTable() {
        return table;
    }

    String getColumns() {
        return columns;
    }
}
