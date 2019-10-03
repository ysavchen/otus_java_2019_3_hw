package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dbservice.DbServiceUser;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserOperationsTests extends ServletTestBase {

    private static Server server;

    @BeforeAll
    static void startServer() throws Exception {
        JettyServer jettyServer = new JettyServer(PORT, Mockito.mock(DbServiceUser.class));
        server = jettyServer.createServer();
        server.start();
    }

    @AfterAll
    static void stopServer() throws Exception {
        server.stop();
    }

    @Test
    void checkUserOperations() throws IOException {
        var connection = connectWithAuth("/userOperations");
        connection.setRequestMethod("GET");

        String response = readResponse(connection);
        String expPage = getResourceContent("static/userOperations.html");

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(),
                "HttpStatus is not OK from /userOperations");
        assertEquals(expPage.replaceAll("\\s+", ""),
                response.replaceAll("\\s+", ""),
                "Response content is not correct");
    }
}
