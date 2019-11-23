package com.mycompany.mutiprocess.ms_client;

import java.net.Socket;
import java.util.UUID;

public interface MsClient {

    UUID getId();

    void registerClient(Socket clientSocket);

    void addHandler(MessageType type, MessageHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    ClientType getType();

    <T> Message produceMessage(ClientType to, T data, MessageType msgType);

}
