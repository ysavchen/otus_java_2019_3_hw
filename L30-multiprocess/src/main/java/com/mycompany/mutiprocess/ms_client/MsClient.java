package com.mycompany.mutiprocess.ms_client;

public interface MsClient {

    void addHandler(MessageType type, MessageHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    ClientType getType();

    Message produceMessage(ClientType to, MessageType msgType);

    <T> Message produceMessage(ClientType to, T data, MessageType msgType);

}
