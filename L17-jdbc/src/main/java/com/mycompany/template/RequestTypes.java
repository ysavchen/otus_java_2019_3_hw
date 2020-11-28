package com.mycompany.template;

enum RequestTypes {

    INSERT("insert"), UPDATE("update"), LOAD("select");

    private final String type;

    RequestTypes(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }
}
