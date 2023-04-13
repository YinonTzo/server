package com.company.services;

import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.entities.Client;
import com.company.entities.ExecutionResult;
import com.company.server.Server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientManagerService {

    Long addClient(Server.ClientHandler client);

    void removeClient(Long clientId, Integer removeMessageId);

    Optional<Client> getClient(Long clientId);

    Integer sendMessage(Long id, BaseServerToClient message) throws IOException;

    Map<Long, ClientAndServerStatus> getAllClientsAndStatuses();

    ClientAndServerStatus getStatus(Long id);

    void addExecutionResult(Long clientId, ExecutionResult executionResult);

    List<Long> getAllClientsIds();

    Optional<List<Integer>> getPayloadsIdsByClientId(Long clientId);

    boolean isConnected(Long clientId);
}