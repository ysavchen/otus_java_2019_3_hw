package com.mycompany.jetty_server;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserOperationsTests extends ServletTestBase {

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
    void checkConnection() throws IOException {
        var connection = connectWithAuth("/userOperations");
        connection.setRequestMethod("GET");

        String response = readResponse(connection);
        String expPage = getResourceContent("static/userOperations.html");

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(),
                "HttpStatus is not OK for /userOperations");
        assertEquals(expPage.replaceAll("\\s+", ""),
                response.replaceAll("\\s+", ""),
                "Response content is not correct");
    }
}
