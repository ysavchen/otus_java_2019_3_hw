package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dao.User;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllUsersDataTests extends ServletTestBase {

    private static Server server;

    @BeforeAll
    static void startServer() throws Exception {
        JettyServer jettyServer = new JettyServer();
        server = jettyServer.createServer(PORT);
        server.start();
    }

    @AfterAll
    static void stopServer() throws Exception {
        server.stop();
    }

    @Test
    void getUserData() throws Exception {
        var storeConnection = connectWithAuth("/userStore");
        storeConnection.setRequestMethod("POST");

        User user = new User()
                .setName("Frank")
                .setSurname("Kenley")
                .setAge(35);

        sendData(gson.toJson(user), storeConnection);
        assertEquals(HttpStatus.OK_200, storeConnection.getResponseCode(),
                "HttpStatus is not OK from /userStore");

        var userDataConnection = connectWithAuth("/allUsersData");
        userDataConnection.setRequestMethod("GET");
        String response = readResponse(userDataConnection);
        assertEquals(HttpStatus.OK_200, userDataConnection.getResponseCode(),
                "HttpStatus is not OK from /allUsersData");
        assertEquals(gson.toJson(List.of(user.setId(1))), response,
                "User data from /allUsersData is not valid");
    }
}
