package com.mycompany.mutiprocess.ms_client;

import java.net.Socket;
import java.util.UUID;

public interface MsClient {

    UUID getId();

    void addHandler(MessageType type, MessageHandler requestHandler);

    void sendMessage(Message msg, Socket clientSocket);

    void handle(Message msg, Socket clientSocket);

    ClientType getType();

    <T> Message produceMessage(ClientType to, T data, MessageType msgType);

}
