package com.mycompany.jetty_server.servlets;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Returns a list of saved users
 */
@Slf4j
public class UserData extends HttpServlet {

    private final SessionFactory sessionFactory;

    private final Gson gson = new Gson();

    public UserData(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] requestParams = request.getPathInfo().split("/");
        String dataParam = "defaultValue";
        if (requestParams.length == 2) {
            dataParam = requestParams[1];
        }

        logger.info("request params:" + dataParam);

        String resultAsString = gson.toJson("from server:" + dataParam);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
