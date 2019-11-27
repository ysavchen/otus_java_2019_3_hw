package com.mycompany.mutiprocess.message_system;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class MessageConsumerImpl implements MessageConsumer {

    private final UUID id = UUID.randomUUID();
    private final ClientType type;
    private final Socket clientSocket;
    private final Gson gson = new Gson();

    public MessageConsumerImpl(ClientType type, Socket clientSocket) {
        this.type = type;
        this.clientSocket = clientSocket;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public ClientType getType() {
        return type;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            logger.info("Sending message: {}", message);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            out.println(gson.toJson(message));
            out.flush();
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageConsumerImpl that = (MessageConsumerImpl) o;
        return Objects.equals(id, that.id) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
