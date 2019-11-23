package com.mycompany.mutiprocess.database;

import com.google.gson.Gson;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MsClient;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DBServer {

    private static final int DB_SERVER_PORT = 8083;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    private final MsClient msClient;
    private final Socket clientSocket;

    private final Gson gson = new Gson();


    DBServer(MsClient dbMsClient, Socket clientSocket) {
        this.msClient = dbMsClient;
        this.clientSocket = clientSocket;
    }

    void start() {
        try (ServerSocket serverSocket = new ServerSocket(DB_SERVER_PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("Database Server waiting for client connection");
                Socket socket = serverSocket.accept();
                executor.submit(() -> clientHandler(socket));
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
        executor.shutdown();
    }

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
