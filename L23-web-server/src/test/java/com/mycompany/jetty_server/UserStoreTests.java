package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dao.User;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserStoreTests extends ServletTestBase {

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
    void saveUser() throws Exception {
        var connection = connectWithAuth("/userStore");
        connection.setRequestMethod("POST");

        User user = new User()
                .setName("Frank")
                .setSurname("Kenley")
                .setAge(35);
        sendData(gson.toJson(user), connection);
        String response = gson.fromJson(readResponse(connection), String.class);

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(),
                "responseCode is not correct for doPost");
        assertEquals("<p>User is saved with id = 1</p>", response,
                "Response is not correct for doPost");
    }

}
