package com.company.services.map;

import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.entities.Client;
import com.company.entities.ExecutionResult;
import com.company.server.Server;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.services.ClientManagerService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class ClientsManager implements ClientManagerService {

    private final List<Client> connectedClients = new ArrayList<>();
    private final ClientHandlers clientHandlers = new ClientHandlers();

    @Override
    public Long addClient(Server.ClientHandler client) {
        Client newClient = new Client();
        Long newClientId = (long) connectedClients.size();
        connectedClients.add(newClient);
        newClient.setId(newClientId);
        clientHandlers.add(newClientId, client);
        log.info("Added new client with id: " + newClientId);
        return newClientId;
    }

    @Override
    public void removeClient(Long clientId, Integer removeMessageId) {
        Server.ClientHandler clientHandler = clientHandlers.getClient(clientId);
        if (clientHandler == null) {
            log.warn("{} are no exists.", clientId);
            return;
        }

        clientHandler.setRemoveMessageId(removeMessageId);
        clientHandlers.remove(clientId);
        log.info("Removed client id: " + clientId);
    }

    @Override
    public Optional<Client> getClient(Long clientId) {
        if (clientId < 0L || clientId >= connectedClients.size()) {
            return Optional.empty();
        }
        return Optional.of(connectedClients.get(clientId.intValue()));
    }

    @Override
    public Integer sendMessage(Long id, BaseServerToClient message) throws IOException {
        clientHandlers.sendMessage(id, message);
        return message.getId();
    }

    @Override
    public Map<Long, ClientAndServerStatus> getAllClientsAndStatuses() {

        Map<Long, ClientAndServerStatus> clientsAndStatuses = new HashMap<>();

        for (Long i = 0L; i < connectedClients.size(); i++) {
            clientsAndStatuses.put(i, getStatus(i));
        }

        return clientsAndStatuses;
    }

    @Override
    public ClientAndServerStatus getStatus(Long id) {
        if (id < 0L || id >= connectedClients.size())
            return null;

        return clientHandlers.contains(id) ? ClientAndServerStatus.AVAILABLE : ClientAndServerStatus.UNAVAILABLE;
    }

    @Override
    public void addExecutionResult(Long clientId, ExecutionResult executionResult) {
        Optional<Client> clientOptional = getClient(clientId);
        if (clientOptional.isPresent()) {
            clientOptional.get().getResults().add(executionResult);
        } else {
            log.warn("There is no client {}", clientId); //TODO: handle it better.
        }
    }

    @Override
    public List<Long> getAllClientsIds() {
        List<Long> allClients = new ArrayList<>();
        for (Long i = 0L; i < connectedClients.size(); i++) {
            allClients.add(i);
        }
        return allClients;
    }

    @Override
    public Optional<List<Integer>> getPayloadsIdsByClientId(Long clientId) {
        if (clientId < 0L || clientId >= connectedClients.size()) {
            return Optional.empty();
        }

        Client wantedClient = connectedClients.get(clientId.intValue());

        List<ExecutionResult> executionResults = wantedClient.getResults();
        List<Integer> executionDataIds = new ArrayList<>();

        for (ExecutionResult executionResult : executionResults) {
            executionDataIds.add(executionResult.getMessageId());
        }
        return Optional.of(executionDataIds);
    }

    public boolean isConnected(Long clientId) {
        return clientHandlers.contains(clientId);
    }
}