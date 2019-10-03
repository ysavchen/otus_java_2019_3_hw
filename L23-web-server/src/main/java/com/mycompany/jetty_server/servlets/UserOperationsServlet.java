package com.mycompany.jetty_server.servlets;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserOperationsServlet extends HttpServlet {

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        URL url = UserOperationsServlet.class.getClassLoader().getResource("static/userOperations.html");
        String page;
        if (url != null) {
            page = Files.readString(Paths.get(url.toURI()));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("userOperations.html file not found");
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.print(page);
        printWriter.flush();
    }
}
