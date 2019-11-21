package com.mycompany.mutiprocess.ms_client;

public enum MessageType {

    STORE_USER("StoreUser"),

    ALL_USERS_DATA("AllUsersData"),

    VOID_TECHNICAL_MESSAGE("voidTechnicalMessage");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
