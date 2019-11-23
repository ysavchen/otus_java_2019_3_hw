package com.mycompany.mutiprocess.ms_client;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.ms_client.common.Serializers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MsClientImpl implements MsClient {

    private final UUID id;
    private PrintWriter out;

    private final ClientType clientType;
    private final Map<MessageType, MessageHandler> handlers = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public MsClientImpl(ClientType clientType) {
        this.id = UUID.randomUUID();
        this.clientType = clientType;
    }

    public MsClientImpl(UUID clientId, ClientType clientType) {
        this.id = clientId;
        this.clientType = clientType;
    }

    public UUID getId() {
        return id;
    }

    @SneakyThrows
    @Override
    public void registerMsClient(Socket clientSocket) {
        this.out = new PrintWriter(clientSocket.getOutputStream());
        Message outMsg = produceMessage(null, null, MessageType.REGISTER_CLIENT);
        sendMessage(outMsg);
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
    public void sendMessage(Message message) {
        out.println(gson.toJson(message));
        out.flush();
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
    public <T> Message produceMessage(ClientType to, T data, MessageType msgType) {
        return new Message(clientType, to, null, id, msgType, Serializers.serialize(data));
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

    @Override
    public String toString() {
        return "MsClientImpl{" +
                "id=" + id +
                ", clientType=" + clientType +
                '}';
    }
}
