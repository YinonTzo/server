package com.company.services.map;

import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.server.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandlers {

    private final Map<Long, Server.ClientHandler> connectedClients = new HashMap<>();

    public void add(Long id, Server.ClientHandler client) {
        connectedClients.put(id, client);
    }

    public void remove(Long id) {
        connectedClients.remove(id);
    }

    public void sendMessage(Long id, BaseServerToClient message) throws IOException {
        Server.ClientHandler wantedClient = connectedClients.get(id);

        if (wantedClient == null) {
            System.out.println("There is no client " + id); //TODO: do it better.
        } else {
            wantedClient.sendMessage(message);
        }
    }

    public boolean contains(Long id) {
        return connectedClients.containsKey(id);
    }

    public Server.ClientHandler getClient(Long clientId) {
        return connectedClients.get(clientId);
    }
}