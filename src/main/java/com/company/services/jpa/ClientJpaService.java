package com.company.services.jpa;

import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.entities.Client;
import com.company.entities.ExecutionResult;
import com.company.repositories.ClientRepository;
import com.company.server.Server;
import com.company.services.ClientManagerService;
import com.company.services.map.ClientHandlers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implements the "ClientManagerService" interface. It provides methods that interact with clients in some way.
 */

@Slf4j
@AllArgsConstructor
public class ClientJpaService implements ClientManagerService {

    private final ClientRepository clientRepository;
    private final ClientHandlers clientHandlers;

    @Override
    public Long addClient(Server.ClientHandler client) {
        Client newClient = clientRepository.save(new Client());
        Long newClientId = newClient.getId();

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
        return clientRepository.findById(clientId);
    }

    @Override
    public Integer sendMessage(Long id, BaseServerToClient message) throws IOException {
        clientHandlers.sendMessage(id, message);
        return message.getId();
    }

    @Override
    public Map<Long, ClientAndServerStatus> getAllClientsAndStatuses() {
        List<Long> connectedClientsIds = getAllClientsIds();

        Map<Long, ClientAndServerStatus> clientsAndStatuses = new HashMap<>();

        for (Long id : connectedClientsIds) {
            clientsAndStatuses.put(id, getStatus(id));
        }

        return clientsAndStatuses;
    }

    @Override
    public ClientAndServerStatus getStatus(Long id) {
        return clientHandlers.contains(id) ? ClientAndServerStatus.AVAILABLE : ClientAndServerStatus.UNAVAILABLE;
    }

    @Override
    public void addExecutionResult(Long clientId, ExecutionResult executionResult) {
        Optional<Client> optionalClient = getClient(clientId);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            List<ExecutionResult> results = client.getResults();
            results.add(executionResult);
            client.setResults(results);

            clientRepository.update(client);
            log.info("Added execution result for client id: " + clientId);
        } else {
            log.warn("Cannot add execution result for non-existent client with id: " + clientId);
        }
    }

    @Override
    public List<Long> getAllClientsIds() {
        return clientRepository.findAllIds();
    }

    @Override
    public Optional<List<Integer>> getPayloadsIdsByClientId(Long clientId) {
        Optional<List<Integer>> optionalResults;

        Optional<Client> optionalClient = getClient(clientId);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            optionalResults = Optional.of(collectIds(client));
        } else {
            log.info("Cannot return results for non-existent client with id: " + clientId);
            optionalResults = Optional.empty();
        }

        return optionalResults;
    }

    private List<Integer> collectIds(Client client) {
        return client.getResults().stream()
                .map(ExecutionResult::getMessageId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConnected(Long clientId) {
        return clientHandlers.contains(clientId);
    }
}