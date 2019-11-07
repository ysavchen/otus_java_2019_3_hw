package com.mycompany.msapp.messageSystem;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(String clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;
}
