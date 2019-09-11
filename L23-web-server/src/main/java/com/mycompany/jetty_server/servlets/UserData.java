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
import java.util.List;

/**
 * Returns a list of saved users
 */
@Slf4j
public class UserData extends HttpServlet {

    private final DbServiceUser dbServiceUser;
    private final Gson gson = new Gson();

    public UserData(DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<User> users = dbServiceUser.getAllUsers();
        String resultAsString = gson.toJson(users);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
