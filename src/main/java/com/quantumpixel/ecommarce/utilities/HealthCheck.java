package com.quantumpixel.ecommarce.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HealthCheck {
    private static final Logger LOGGER = Logger.getLogger(HealthCheck.class.getName());

    public static void main(String[] args) {
        if (args.length != 3) {
            System.exit(-1);
        }

        String host = args[0];
        int port = 0;
        String path = args[2];
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing port: {0}", e.getMessage());
            System.exit(-2);
        }

        try {
            // Attempt a TCP socket connection
            int responseCode = getResponseCode(host, port, path);
            if (responseCode >= 200 && responseCode < 300) {
                System.exit(0); // Health check passed
            } else {
                LOGGER.log(Level.SEVERE,"HTTP Health check failed with response code: {0}", responseCode);
                System.exit(1);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error during health check: {0}", e.getMessage());
            System.exit(1);
        }
    }

    private static int getResponseCode(String host, int port, String path) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 10 * 1000); // You may adjust the timeout
        }

        // If the socket connection is successful, try an HTTP connection with a specified path
        URL url = new URL("http", host, port, path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection.getResponseCode();
    }
}
