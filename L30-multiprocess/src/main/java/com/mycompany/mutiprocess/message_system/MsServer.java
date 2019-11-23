package com.mycompany.mutiprocess.message_system;

import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class MsServer {

    private static final int PORT = 8080;
    private MessageSystem messageSystem;

    public static void main(String[] args) {
        new MsServer().start();
    }

    private void start() {
        messageSystem = new MessageSystemImpl();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    private void clientHandler(Socket clientSocket) {
        try {
            try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                Message message = (Message) ois.readObject();
                if (message.getType() == MessageType.REGISTER_CLIENT) {
                    messageSystem.addClient(new MsClientImpl(message.getFromClientId(), message.getFrom()));
                } else {
                    messageSystem.newMessage(message);
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
