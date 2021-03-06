package com.mycompany.msapp.messageSystem;

public enum MessageType {
    STORE_USER("StoreUser"),

    ALL_USERS_DATA("AllUsersData");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
