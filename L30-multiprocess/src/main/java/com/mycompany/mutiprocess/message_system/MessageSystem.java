package com.mycompany.mutiprocess.message_system;

import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MsClient;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(ClientType clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;
}
