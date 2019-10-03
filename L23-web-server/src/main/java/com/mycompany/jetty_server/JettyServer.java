package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dbservice.DbServiceUser;
import com.mycompany.jetty_server.servlets.UserDataServlet;
import com.mycompany.jetty_server.servlets.UserOperationsServlet;
import com.mycompany.jetty_server.servlets.UserStoreServlet;
import lombok.SneakyThrows;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JettyServer {

    private final int port;
    private final DbServiceUser dbServiceUser;

    public JettyServer(int port, DbServiceUser dbServiceUser) {
        this.port = port;
        this.dbServiceUser = dbServiceUser;
    }

    public void start() throws Exception {
        Server server = createServer();
        server.start();
        server.join();
    }

    Server createServer() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UserOperationsServlet()), "/userOperations");
        context.addServlet(new ServletHolder(new UserDataServlet(dbServiceUser)), "/userData");
        context.addServlet(new ServletHolder(new UserStoreServlet(dbServiceUser)), "/userStore");

        Server server = new Server(port);
        server.setHandler(new HandlerList(context));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler(), createSecurityHandler(context)});
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        URL fileDir = JettyServer.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        String decodedUrl = URLDecoder.decode(fileDir.getPath(), StandardCharsets.UTF_8);
        resourceHandler.setResourceBase(decodedUrl);
        return resourceHandler;
    }

    private SecurityHandler createSecurityHandler(ServletContextHandler context) {
        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        security.setLoginService(new HashLoginService("MyRealm", getRealmPropsPath()));
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(List.of(getConstraintMapping()));

        return security;
    }

    @SneakyThrows
    private String getRealmPropsPath() {
        URL propFile = null;
        File realmFile = new File("./realm.properties");
        if (realmFile.exists()) {
            propFile = realmFile.toURI().toURL();
        }
        if (propFile == null) {
            System.out.println("local realm config not found, looking into Resources");
            propFile = JettyServer.class.getClassLoader().getResource("realm.properties");
        }

        if (propFile == null) {
            throw new FileNotFoundException("Realm property file not found");
        }

        return URLDecoder.decode(propFile.getPath(), StandardCharsets.UTF_8);
    }

    private ConstraintMapping getConstraintMapping() {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);
        return mapping;
    }

}
