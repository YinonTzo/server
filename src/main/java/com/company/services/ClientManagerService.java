package com.company.services;

import com.company.common.statuses.ClientAndServerStatus;
import com.company.server.Server;

import java.util.List;
import java.util.Map;

public interface ClientManagerService {

    int addClient(Server.ClientHandler client);

    Server.ClientHandler getClient(int id);

    List<Server.ClientHandler> getAllAvailableClients();

    List<Server.ClientHandler> getAllClients();

    Map<Integer, ClientAndServerStatus> getAllClientsAndStatuses();
}
