package com.mycompany.mutiprocess.ms_client;

import com.mycompany.mutiprocess.ms_client.common.Serializers;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MsClientImpl implements MsClient {

    private final ClientType clientType;
    private final Map<MessageType, MessageHandler> handlers = new ConcurrentHashMap<>();

    public MsClientImpl(ClientType clientType) {
        this.clientType = clientType;
    }

    @Override
    public void addHandler(MessageType type, MessageHandler requestHandler) {
        this.handlers.put(type, requestHandler);
    }

    @Override
    public ClientType getType() {
        return clientType;
    }

    @Override
    public boolean sendMessage(Message msg) {


        //send message to MessageSystem
        boolean result = false;

        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            MessageHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
    }

    @Override
    public Message produceMessage(ClientType to, MessageType msgType) {
        return new Message(clientType, to, null, msgType, Serializers.serialize(""));
    }

    @Override
    public <T> Message produceMessage(ClientType to, T data, MessageType msgType) {
        return new Message(clientType, to, null, msgType, Serializers.serialize(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(clientType, msClient.clientType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientType);
    }
}
