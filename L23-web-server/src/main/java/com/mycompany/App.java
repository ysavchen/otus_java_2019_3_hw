package com.mycompany;

import com.mycompany.servlets.UserOperations;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.MalformedURLException;
import java.net.URL;

public class App {

    private final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    private void start() throws Exception {
        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    private Server createServer(int port) throws MalformedURLException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UserOperations()), "/userOperations");

        Server server = new Server(port);
        server.setHandler(new HandlerList(context));
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler()});
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        URL fileDir = App.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(fileDir.getPath());
        return resourceHandler;
    }
}
