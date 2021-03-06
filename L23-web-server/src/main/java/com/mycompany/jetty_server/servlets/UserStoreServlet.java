package com.mycompany.jetty_server.servlets;

import com.google.gson.Gson;
import com.mycompany.jetty_server.dao.User;
import com.mycompany.jetty_server.dbservice.DbServiceUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class UserStoreServlet extends HttpServlet {

    private final DbServiceUser dbServiceUser;
    private final Gson gson = new Gson();

    public UserStoreServlet(DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder json = new StringBuilder();
        String userData;
        while ((userData = request.getReader().readLine()) != null) {
            json.append(userData);
        }

        String message = storeUser(gson.fromJson(json.toString(), User.class));
        sendResponse(response, message);
    }

    private String storeUser(User user) {
        String message;
        try {
            long id = dbServiceUser.saveUser(user);

            message = "User is saved with id = " + id;
        } catch (Exception ex) {
            logger.error("Error: " + ex);
            message = "User is not saved. \n Error: " + ex.getCause();
        }
        return message;
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        String resultAsString = gson.toJson(message);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
