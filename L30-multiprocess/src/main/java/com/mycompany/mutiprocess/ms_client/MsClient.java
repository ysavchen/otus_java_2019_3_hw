package com.mycompany.mutiprocess.ms_client;

import java.net.Socket;
import java.util.UUID;

public interface MsClient {

    UUID getId();

    void registerMsClient(Socket clientSocket);

    void addHandler(MessageType type, MessageHandler requestHandler);

    void sendMessage(Message msg);

    void handle(Message msg);

    ClientType getType();

    <T> Message produceMessage(ClientType to, T data, MessageType msgType);

}
