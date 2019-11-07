package com.mycompany.msapp.domain;

public class WSMessage {

    private final String infoContent;
    private final String usersDataContent;

    public WSMessage(String infoContent, String usersDataContent) {
        this.infoContent = infoContent;
        this.usersDataContent = usersDataContent;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public String getUsersDataContent() {
        return usersDataContent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "infoContent='" + infoContent + '\'' +
                ", usersDataContent='" + usersDataContent + '\'' +
                '}';
    }
}
