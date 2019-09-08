package com.mycompany.jetty_server.servlets;

import com.mycompany.jetty_server.JettyServer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class UserOperations extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        URL file = JettyServer.class.getClassLoader().getResource("userOperations.html");
        String resultAsString = "";
        if (file != null) {
            resultAsString = file.toString();
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resultAsString);
        printWriter.flush();
    }
}
