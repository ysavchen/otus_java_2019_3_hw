package com.mycompany.mutiprocess.message_system;

public interface MsClient {

    void addHandler(MessageType type, MessageHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    Message produceMessage(String to, MessageType msgType);

    <T> Message produceMessage(String to, T data, MessageType msgType);
}
