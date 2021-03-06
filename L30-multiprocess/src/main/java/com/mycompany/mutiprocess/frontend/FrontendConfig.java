package com.mycompany.mutiprocess.frontend;

import com.mycompany.mutiprocess.frontend.handlers.GetAllUsersResponseHandler;
import com.mycompany.mutiprocess.frontend.handlers.StoreUserResponseHandler;
import com.mycompany.mutiprocess.frontend.service.FrontendService;
import com.mycompany.mutiprocess.frontend.service.FrontendServiceImpl;
import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

@EnableAsync
@Configuration
public class FrontendConfig {

    private static final int MS_PORT = 8081;
    private static final String HOST = "localhost";

    @Bean
    public Socket clientSocket() throws IOException {
        return new Socket(HOST, MS_PORT);
    }

    @Bean
    public MsClient frontendMsClient(Socket clientSocket) {
        return new MsClientImpl(clientSocket, ClientType.FRONTEND_SERVICE);
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, ClientType.DATABASE_SERVICE);
        frontendMsClient.addHandler(MessageType.STORE_USER, new StoreUserResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersResponseHandler(frontendService));
        return frontendService;
    }

    @Bean
    public FrontendServer frontendServer(MsClient frontendMsClient) {
        int serverPort = getRandomPort(8285, 8385);
        FrontendServer server = new FrontendServer(serverPort, frontendMsClient);
        server.start();
        return server;
    }

    private int getRandomPort(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
