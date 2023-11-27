package com.nick.server;

import com.nick.server.server_utilities.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(12346);
        server.run();
    }
}
