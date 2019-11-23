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
public class DatabaseServer {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final int DB_SERVER_PORT = 8083;
    private final MsClient dbMsClient;
    private final Gson gson = new Gson();
    private final Socket msClientSocket;

    DatabaseServer(MsClient dbMsClient, Socket msClientSocket) {
        this.dbMsClient = dbMsClient;
        this.msClientSocket = msClientSocket;
    }

    void start() {
        try (ServerSocket serverSocket = new ServerSocket(DB_SERVER_PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> clientHandler(clientSocket));
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
        executor.shutdown();
    }

    private void clientHandler(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            while (true) {
                String input = in.readLine();
                if (input != null) {
                    Message message = gson.fromJson(input, Message.class);
                    dbMsClient.handle(message, msClientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
