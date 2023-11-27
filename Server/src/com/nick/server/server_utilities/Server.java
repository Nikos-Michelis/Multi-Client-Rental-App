package com.nick.server.server_utilities;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private Set<Handler> users = Collections.synchronizedSet(new HashSet<>());

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running and listening on port..." + serverSocket.getLocalPort());
        } catch (IOException e) {
            System.err.println("Error... " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void run() {
        ExecutorService es = Executors.newFixedThreadPool(1000);
        while (true) {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("client connected: " + clientSocket.getInetAddress());
                    Handler handler = new Handler(clientSocket, users);
                    users.add(handler);
                    es.execute(handler);
                }
            } catch (IOException e) {
                System.out.println("Error from run.." + e);
                e.printStackTrace();
            }
        }
    }
}
