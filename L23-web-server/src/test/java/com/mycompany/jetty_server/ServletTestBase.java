package com.mycompany.jetty_server;

import com.google.gson.Gson;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class ServletTestBase {

    final Gson gson = new Gson();
    static final int PORT = 8081;

    @SneakyThrows
    HttpURLConnection connectWithAuth(String part) {
        var connection = (HttpURLConnection) new URL("http://localhost:" + PORT + part)
                .openConnection();

        Authenticator auth = new Authenticator() {

            @SneakyThrows
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                Properties prop = new Properties();

                var inputStream = JettyServer.class.getClassLoader().getResourceAsStream("realm.properties");
                if (inputStream != null) {
                    prop.load(inputStream);
                } else {
                    throw new FileNotFoundException("realm.properties not found");
                }

                String[] str = prop.getProperty("admin").split(",");

                return new PasswordAuthentication(str[1], str[0].toCharArray());
            }
        };
        connection.setAuthenticator(auth);
        return connection;
    }

    @SneakyThrows
    void sendData(String data, HttpURLConnection connection) {
        if (!connection.getDoOutput()) {
            connection.setDoOutput(true);
        }

        var writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        try (writer) {
            writer.write(data);
        }
    }

    @SneakyThrows
    String readResponse(HttpURLConnection connection) {
        var stringBuilder = new StringBuilder();
        var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        try (reader) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    @SneakyThrows
    String getResourceContent(String resource) {
        URL url = JettyServer.class.getClassLoader().getResource(resource);
        String str;
        if (url != null) {
            str = Files.readString(Paths.get(url.toURI()));
        } else {
            throw new AssertionError(resource + "is not found");
        }
        return str;
    }

}
