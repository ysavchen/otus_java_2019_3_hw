package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dao.User;
import com.mycompany.jetty_server.dbservice.DbServiceUser;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserStoreServletTests extends ServletTestBase {

    private static Server server;
    private static DbServiceUser dbServiceUser;

    @BeforeAll
    static void startServer() throws Exception {
        dbServiceUser = Mockito.mock(DbServiceUser.class);
        JettyServer jettyServer = new JettyServer(PORT, dbServiceUser);
        server = jettyServer.createServer();
        server.start();
    }

    @AfterAll
    static void stopServer() throws Exception {
        server.stop();
    }

    @Test
    void saveUserSuccess() throws Exception {
        var connection = connectWithAuth("/userStore");
        connection.setRequestMethod("POST");

        long id = 1L;
        sendData("{}", connection);
        when(dbServiceUser.saveUser(new User())).thenReturn(id);
        String response = gson.fromJson(readResponse(connection), String.class);

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(),
                "HttpStatus is not OK from /userStore");
        assertEquals("User is saved with id = " + id, response,
                "Response is not correct from /userStore");
    }

    @Test
    void saveUserFailure() throws Exception {
        var connection = connectWithAuth("/userStore");
        connection.setRequestMethod("POST");


        sendData("{}", connection);
        when(dbServiceUser.saveUser(new User())).thenThrow(new RuntimeException());
        String response = gson.fromJson(readResponse(connection), String.class);

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(),
                "HttpStatus is not OK from /userStore");
        System.out.println(response);
        assertEquals("User is not saved. \n Error: null", response,
                "Response for error is not correct from /userStore");
    }

}
