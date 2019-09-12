package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dao.User;
import com.mycompany.jetty_server.dbservice.DbServiceUser;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserDataTests extends ServletTestBase {

    private static Server server;
    private static DbServiceUser dbServiceUser;

    @BeforeAll
    static void startServer() throws Exception {
        dbServiceUser = Mockito.mock(DbServiceUser.class);
        JettyServer jettyServer = new JettyServer(dbServiceUser);
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
                .setId(1L)
                .setName("Daniel")
                .setSurname("Anderson")
                .setAge(20);

        sendData("{}", storeConnection);
        when(dbServiceUser.saveUser(user)).thenReturn(user.getId());
        assertEquals(HttpStatus.OK_200, storeConnection.getResponseCode(),
                "HttpStatus is not OK from /userStore");

        var userDataConnection = connectWithAuth("/userData");
        userDataConnection.setRequestMethod("GET");

        when(dbServiceUser.getAllUsers()).thenReturn(List.of(user));
        String response = readResponse(userDataConnection);
        assertEquals(HttpStatus.OK_200, userDataConnection.getResponseCode(),
                "HttpStatus is not OK from /userData");
        assertEquals(gson.toJson(List.of(user)), response,
                "User data from /userData is not valid");
    }
}
