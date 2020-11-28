package com.mycompany.mutiprocess.frontend;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class FrontendServer {

    private final int serverPort;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final MsClient msClient;
    private final Gson gson = new Gson();

    public FrontendServer(int serverPort, MsClient msClient) {
        this.serverPort = serverPort;
        this.msClient = msClient;
    }

    public void start() {
        Message regServerMsg = msClient.produceMessage(null, serverPort, MessageType.REGISTER_MESSAGE_CONSUMER);
        msClient.sendMessage(regServerMsg);

        //executor.submit() added before ServerSocket created as Spring blocks further initialization on serverSocket.accept()
        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("waiting for client connection");
                    Socket socket = serverSocket.accept();
                    clientHandler(socket);
                }
            } catch (Exception ex) {
                logger.error("error", ex);
            }
        });
        executor.shutdown();
    }

    //gets input data from the connected socket and sends to clientSocket
    private void clientHandler(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
                String input = in.readLine();
                if (input != null) {
                    try {
                        Message message = gson.fromJson(input, Message.class);
                        logger.info("Got message from queue: {}", message);
                        msClient.handle(message);
                    } catch (Exception ex) {
                        logger.error("error", ex);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
