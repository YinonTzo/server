package com.company;

import com.company.config.Configuration;
import com.company.server.Server;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = Configuration.load();
        int serverClientsPort = Integer.parseInt(properties.getProperty("SERVER_CLIENTS_PORT"));
        int serverCliPort = Integer.parseInt(properties.getProperty("SERVER_CLI_PORT"));

        Server server = new Server();
        server.start(serverClientsPort, serverCliPort);
    }
}
