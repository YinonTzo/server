package com.company.services.map;

import com.company.server.Server;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.services.ClientManagerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientsManager  implements ClientManagerService {

    private final List<Server.ClientHandler> clientHandlers = new ArrayList<>();

    @Override
    public int addClient(Server.ClientHandler client) {
        client.setClientId(clientHandlers.size());
        System.out.println("Added new client with id: " + client.getClientId()); //TODO: will be in a logger.
        clientHandlers.add(client);
        return client.getClientId();
    }

    @Override
    public Server.ClientHandler getClient(int id){
        if(id < 0 || id >= clientHandlers.size())
            return null;

        return clientHandlers.get(id);
    }

    @Override
    public List<Server.ClientHandler> getAllAvailableClients() {
        return clientHandlers.stream()
                .filter(Server.ClientHandler::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Server.ClientHandler> getAllClients() {
        return new ArrayList<>(clientHandlers);
    }

    @Override
    public Map<Integer, ClientAndServerStatus> getAllClientsAndStatuses() {

        Map<Integer, ClientAndServerStatus> clientsAndStatuses = new HashMap<>();

        for(int i = 0; i < clientHandlers.size(); i++){
            clientsAndStatuses.put(i, clientHandlers.get(i).getStatus());
        }

        return clientsAndStatuses;
    }
}
