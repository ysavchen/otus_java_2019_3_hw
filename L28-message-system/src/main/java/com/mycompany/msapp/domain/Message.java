package com.mycompany.msapp.domain;

public class Message {

    private String newUserContent;

    public Message() {
    }

    public Message(String newUserContent) {
        this.newUserContent = newUserContent;
    }

    public String getNewUserContent() {
        return newUserContent;
    }

    public void setNewUserContent(String newUserContent) {
        this.newUserContent = newUserContent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "newUserContent='" + newUserContent + '\'' +
                '}';
    }
}
