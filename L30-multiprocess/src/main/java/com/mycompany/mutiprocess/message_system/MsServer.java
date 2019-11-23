package com.mycompany.mutiprocess.message_system;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class MsServer {

    private static final int PORT = 8081;
    private MessageSystem messageSystem;

    private final Gson gson = new Gson();

    public static void main(String[] args) {
        new MsServer().start();
    }

    private void start() {
        messageSystem = new MessageSystemImpl();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    //new Thread(() -> clientHandler(clientSocket)).start();
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    private void clientHandler(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            MsClient msClient = null;

            while (true) {
                String input = in.readLine();
                if (input != null) {
                    Message message = gson.fromJson(input, Message.class);
                    if (message.getType() == MessageType.REGISTER_CLIENT) {

                        msClient = new MsClientImpl(message.getFromClientId(), message.getFrom());
                        messageSystem.addClient(msClient);
                    } else if (message.getType() == MessageType.REMOVE_CLIENT) {
                        messageSystem.removeClient(msClient);
                        break;
                    } else {
                        messageSystem.newMessage(message);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
