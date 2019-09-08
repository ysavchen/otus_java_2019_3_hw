package com.mycompany.jetty_server.servlets;

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
import java.util.List;

@Slf4j
public class UserStore extends HttpServlet {

    private final DbServiceUser dbServiceUser;

    public UserStore(SessionFactory sessionFactory) {
        this.dbServiceUser = new DbServiceUserImpl(sessionFactory);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String age = request.getParameter("age");

        User user = createUser(name, surname, age);
        long id = dbServiceUser.saveUser(user);

        String message = "";
        if (id == 0L) {
            message = "User is not saved.";
        } else {
            message = "User is saved with id = " + id;
        }
        String resultAsString = "<p>" + message + "</p>";

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }

    private User createUser(String name, String surname, String age) {
        List.of(name, surname, age).forEach(value -> {
            if (value == null || "".equals(value.trim())) {
                value = "Undefined";
            }
        });

        int ageInt = 0;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException ex) {
            logger.warn("Invalid value for age: " + age);
        }

        return new User()
                .setName(name)
                .setSurname(surname)
                .setAge(ageInt);
    }
}
