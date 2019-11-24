package com.mycompany.mutiprocess.database;

import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.MsClientImpl;

import java.net.Socket;
import java.util.Random;

/**
 * Database must be started in a server mode via h2/h2.bat file
 */
public class DBStarter {

    private static final int MS_PORT = 8081;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        MsClient dbMsClient = new MsClientImpl(ClientType.DATABASE_SERVICE);
        Socket socket = new Socket(HOST, MS_PORT);

        new DBClient(dbMsClient, socket).start();

        int serverPort = getRandomPort(8085, 8185);
        new DBServer(serverPort, dbMsClient, socket).start();
    }

    private static int getRandomPort(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
