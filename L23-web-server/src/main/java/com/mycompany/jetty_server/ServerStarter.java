package com.mycompany.jetty_server;

public class ServerStarter {

    public static void main(String[] args) throws Exception {

        new JettyServer(8080, DbUtils.connectToDb()).start();
    }
}
