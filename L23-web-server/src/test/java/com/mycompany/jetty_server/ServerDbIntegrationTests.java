package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dao.User;
import com.mycompany.jetty_server.dbservice.DbServiceUser;
import com.mycompany.jetty_server.exceptions.NoDataFoundException;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerDbIntegrationTests extends ServletTestBase {

    private static Server server;
    private static DbServiceUser dbServiceUser;

    @BeforeAll
    static void startServer() throws Exception {
        dbServiceUser = DbUtils.connectToDb();
        JettyServer jettyServer = new JettyServer(dbServiceUser);
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
                "HttpStatus is not OK from /userStore");

        long id = dbServiceUser.getAllUsers()
                .stream()
                .filter(dbUser -> "Kenley".equals(dbUser.getSurname()))
                .filter(dbUser -> "Frank".equals(dbUser.getName()))
                .filter(dbUser -> Integer.valueOf(35).equals(dbUser.getAge()))
                .findFirst()
                .map(User::getId)
                .orElseThrow(NoDataFoundException::new);

        assertEquals("User is saved with id = " + id, response,
                "Response is not correct for doPost");
    }

    @Test
    void getUserData() throws Exception {
        var storeConnection = connectWithAuth("/userStore");
        storeConnection.setRequestMethod("POST");

        User user = new User()
                .setName("Daniel")
                .setSurname("Anderson")
                .setAge(20);

        sendData(gson.toJson(user), storeConnection);
        assertEquals(HttpStatus.OK_200, storeConnection.getResponseCode(),
                "HttpStatus is not OK from /userStore");

        var userDataConnection = connectWithAuth("/userData");
        userDataConnection.setRequestMethod("GET");
        String response = readResponse(userDataConnection);
        assertEquals(HttpStatus.OK_200, userDataConnection.getResponseCode(),
                "HttpStatus is not OK from /userData");

        List<User> userList = dbServiceUser.getAllUsers();
        User savedUser = userList.stream()
                .filter(dbUser -> "Anderson".equals(dbUser.getSurname()))
                .filter(dbUser -> "Daniel".equals(dbUser.getName()))
                .filter(dbUser -> Integer.valueOf(20).equals(dbUser.getAge()))
                .findFirst()
                .orElseThrow(NoDataFoundException::new);

        assertEquals(gson.toJson(userList), response,
                "User data from /userData is not valid");
        assertTrue(response.contains(gson.toJson(savedUser)),
                "No saved user in the response from /userData");
    }
}
