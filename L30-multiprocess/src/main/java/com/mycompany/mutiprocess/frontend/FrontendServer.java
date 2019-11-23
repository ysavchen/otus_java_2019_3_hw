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
    private final Socket clientSocket;
    private final Gson gson = new Gson();

    public FrontendServer(int serverPort, MsClient msClient, Socket clientSocket) {
        this.serverPort = serverPort;
        this.msClient = msClient;
        this.clientSocket = clientSocket;
    }

    public void start() {
        Message regServerMsg = msClient.produceMessage(null, serverPort, MessageType.REGISTER_MESSAGE_CONSUMER);
        msClient.sendMessage(regServerMsg, clientSocket);

        //first executor.submit() added as Spring blocks further initialization on serverSocket.accept()
        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("Frontend Server waiting for client connection");
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
                    Message message = gson.fromJson(input, Message.class);
                    msClient.handle(message, clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
