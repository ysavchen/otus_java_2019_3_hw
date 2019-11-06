package com.mycompany.msapp.domain;

public class Message {

    private final String infoContent;
    private final String userDataContent;

    public Message(String infoContent, String userDataContent) {
        this.infoContent = infoContent;
        this.userDataContent = userDataContent;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public String getUserDataContent() {
        return userDataContent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "infoContent='" + infoContent + '\'' +
                ", userDataContent='" + userDataContent + '\'' +
                '}';
    }
}
