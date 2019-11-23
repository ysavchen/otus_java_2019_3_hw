package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;

import java.net.Socket;

public class DBStarter {

    private static final int MS_PORT = 8081;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        MsClient dbMsClient = new MsClientImpl(ClientType.DATABASE_SERVICE);
        Socket socket = new Socket(HOST, MS_PORT);

        new DBClient(dbMsClient, socket).start();
        new DBServer(dbMsClient, socket).start();
    }
}
