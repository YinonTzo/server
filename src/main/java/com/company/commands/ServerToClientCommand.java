package com.company.commands;

import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.TextMessage;
import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.server.Server;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is an abstract class, which implements the Command interface.
 * It provides a skeleton implementation of a command that can be executed by a server to send messages to clients.
 */

@Slf4j
public abstract class ServerToClientCommand implements Command {

    protected ClientManagerService clientManagerService;
    protected ExecutionResultService executionResultService;

    public ServerToClientCommand(ClientManagerService clientManagerService,
                                 ExecutionResultService executionResultService) {
        this.clientManagerService = clientManagerService;
        this.executionResultService = executionResultService;
    }

    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        //collect available clients
        log.info(cliRequest.toString());

        TextMessage returnedMessage = new TextMessage();
        returnedMessage.setType(cliRequest.getType());

        List<Server.ClientHandler> availableWantedClients =
                collectAvailableWantedClients(cliRequest.getRequestIds());

        if (availableWantedClients.isEmpty()) {
            returnedMessage.setText(String.format("%s are not available clients.", cliRequest.getRequestIds()));
            return returnedMessage;
        }

        //send messages
        Map<Server.ClientHandler, Integer> clientToMessageId;
        try {
            clientToMessageId = sendPayloadToAvailableClients(cliRequest, availableWantedClients);
        } catch (IOException e) {
            log.error(e.getMessage());
            List<Integer> clientIds = availableWantedClients.stream()
                    .map(Server.ClientHandler::getClientId)
                    .toList();
            returnedMessage.setText(String.format("Failed to send message to clients: %s\n Please do exit.", clientIds));
            return returnedMessage;
        }

        //update the server
        returnedMessage.setText(updateServerAndReturnAnswer(clientToMessageId));
        return returnedMessage;
    }

    private Map<Server.ClientHandler, Integer> sendPayloadToAvailableClients(
            BaseCLIToServer cliRequest,
            List<Server.ClientHandler> availableWantedClients) throws IOException {

        BaseServerToClient serverToClients = CLIToServerToServerToClients(cliRequest);

        Map<Server.ClientHandler, Integer> clientToMessageId = new HashMap<>();

        for (Server.ClientHandler client : availableWantedClients) {
            ExecutionData added = executionResultService.save(client);
            serverToClients.setId(added.getMessageId());
            client.sendMessage(serverToClients);
            clientToMessageId.put(client, added.getMessageId());
        }

        return clientToMessageId;
    }

    protected abstract BaseServerToClient CLIToServerToServerToClients(BaseCLIToServer cliRequest);

    protected abstract String updateServerAndReturnAnswer(Map<Server.ClientHandler, Integer> clientToMessageId);

    private List<Server.ClientHandler> collectAvailableWantedClients(List<Integer> wantedClientsIds) {
        if (wantedClientsIds.contains(-1)) { //broadcast
            return clientManagerService.getAllAvailableClients();
        }

        return wantedClientsIds.stream()
                .map(clientManagerService::getClient)
                .filter(Objects::nonNull)
                .filter(Server.ClientHandler::isAvailable)
                .collect(Collectors.toList());
    }
}