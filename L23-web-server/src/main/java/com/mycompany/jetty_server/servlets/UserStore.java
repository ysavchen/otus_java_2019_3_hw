package com.mycompany.jetty_server.servlets;

import com.google.gson.Gson;
import com.mycompany.jetty_server.dao.User;
import com.mycompany.jetty_server.dbservice.DbServiceUser;
import com.mycompany.jetty_server.dbservice.DbServiceUserImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class UserStore extends HttpServlet {

    private final DbServiceUser dbServiceUser;
    private final Gson gson = new Gson();

    public UserStore(SessionFactory sessionFactory) {
        this.dbServiceUser = new DbServiceUserImpl(sessionFactory);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder json = new StringBuilder();
        String userData;
        while ((userData = request.getReader().readLine()) != null) {
            json.append(userData);
        }

        User user = gson.fromJson(json.toString(), User.class);
        long id = dbServiceUser.saveUser(user);

        String message;
        if (id == 0L) {
            message = "User is not saved.";
        } else {
            message = "User is saved with id = " + id;
        }

        String resultAsString = gson.toJson("<p>" + message + "</p>");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
