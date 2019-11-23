package com.mycompany.mutiprocess.message_system;

import com.mycompany.mutiprocess.ms_client.ClientType;

import java.net.Socket;
import java.util.Objects;
import java.util.UUID;

public class MessageConsumerImpl implements MessageConsumer {

    private final UUID id = UUID.randomUUID();
    private final ClientType type;
    private final Socket clientSocket;

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
    public Socket getClientSocket() {
        return clientSocket;
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
