package com.mycompany.mutiprocess.message_system;

import com.mycompany.mutiprocess.ms_client.ClientType;

import java.net.Socket;
import java.util.UUID;

public interface MessageConsumer {

    UUID getId();

    ClientType getType();

    Socket getClientSocket();

}
