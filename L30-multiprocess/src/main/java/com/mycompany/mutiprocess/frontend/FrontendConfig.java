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

@EnableAsync
@Configuration
public class FrontendConfig {

    private static final int MS_PORT = 8081;
    private static final String HOST = "localhost";

    @Bean
    public MsClient frontendMsClient() {
        return new MsClientImpl(ClientType.FRONTEND_SERVICE);
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
        FrontendServer server = new FrontendServer(frontendMsClient);
        server.start();
        return server;
    }
}
