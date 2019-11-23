package com.mycompany.mutiprocess.message_system;

import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MsClient;

import java.net.Socket;

public interface MessageSystem {

    void addClient(MsClient msClient, Socket clientSocket);

    void removeClient(MsClient msClient);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;
}
