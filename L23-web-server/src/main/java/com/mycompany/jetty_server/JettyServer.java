package com.mycompany.jetty_server;

import com.mycompany.jetty_server.dbservice.DbServiceUser;
import com.mycompany.jetty_server.servlets.UserData;
import com.mycompany.jetty_server.servlets.UserOperations;
import com.mycompany.jetty_server.servlets.UserStore;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class JettyServer {

    private final static int PORT = 8080;

    private final DbServiceUser dbServiceUser;

    public static void main(String[] args) throws Exception {

        new JettyServer(DbUtils.connectToDb()).start();
    }

    public JettyServer(DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    private void start() throws Exception {
        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    Server createServer(int port) throws MalformedURLException, FileNotFoundException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UserOperations()), "/userOperations");
        context.addServlet(new ServletHolder(new UserData(dbServiceUser)), "/userData");
        context.addServlet(new ServletHolder(new UserStore(dbServiceUser)), "/userStore");

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
        resourceHandler.setResourceBase(fileDir.getPath());
        return resourceHandler;
    }

    private SecurityHandler createSecurityHandler(ServletContextHandler context) throws MalformedURLException, FileNotFoundException {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

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

        security.setLoginService(new HashLoginService("MyRealm", propFile.getPath()));
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }

}
