package com.mycompany;

import org.eclipse.jetty.server.Server;

public class App {

    private final static int PORT = 8080;

    public static void main(String[] args) {
        new App().start();
    }

    private void start() {
        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    private Server createServer(int port) {

    }
}
