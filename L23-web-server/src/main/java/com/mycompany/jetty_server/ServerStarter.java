package com.mycompany.jetty_server;

public class ServerStarter {

    public static void main(String[] args) {

        new JettyServer(8080, DbUtils.connectToDb()).start();
    }
}
