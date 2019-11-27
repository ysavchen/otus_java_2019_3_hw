package com.mycompany.mutiprocess.message_system;

import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.Message;

import java.util.UUID;

public interface MessageConsumer {

    UUID getId();

    ClientType getType();

    void sendMessage(Message message);

}
