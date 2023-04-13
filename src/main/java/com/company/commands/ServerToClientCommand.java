package com.company.commands;

import com.company.collectors.ClientsCollector;
import com.company.common.messages.serverToCLI.SendPayload;
import com.company.entities.ExecutionResult;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToClient.BaseServerToClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * This is an abstract class, which implements the Command interface.
 * It provides a skeleton implementation of a command that can be executed by a server to send messages to clients.
 */

@Slf4j
public abstract class ServerToClientCommand implements Command {

    protected ClientManagerService clientManagerService;
    protected ExecutionResultService executionResultService;
    private final ClientsCollector clientsCollector;

    public ServerToClientCommand(ClientManagerService clientManagerService,
                                 ExecutionResultService executionResultService) {
        this.clientManagerService = clientManagerService;
        this.executionResultService = executionResultService;

        this.clientsCollector = new ClientsCollector(clientManagerService);
    }

    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        log.info(cliRequest.toString());

        SendPayload returnedMessage = new SendPayload();
        returnedMessage.setType(cliRequest.getType());

        Map<Long, Integer> clientIdToMessageId;
        try {
            clientIdToMessageId = sendPayloadToWantedClients(cliRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error(String.format("Failed to send message to clients: %s", cliRequest.getRequestIds()));

            returnedMessage.setClientIdToAck(null);
            return returnedMessage;
        }

        returnedMessage.setClientIdToAck(updateServerAndReturnAnswer(clientIdToMessageId));
        return returnedMessage;
    }

    private Map<Long, Integer> sendPayloadToWantedClients(BaseCLIToServer cliRequest) throws IOException {

        BaseServerToClient serverToClient = CLIToServerToServerToClients(cliRequest);

        List<Long> wantedClientsIds = clientsCollector.collect(cliRequest.getRequestIds());

        Map<Long, Integer> clientIdToMessageId = new HashMap<>();

        for (Long clientId : wantedClientsIds) {
            if (!clientManagerService.isConnected(clientId)) {
                clientIdToMessageId.put(clientId, null);
            } else {
                //save the message in the db and get id for the payload
                ExecutionResult added = executionResultService.save(clientId);

                serverToClient.setId(added.getMessageId());
                clientManagerService.sendMessage(clientId, serverToClient);
                clientIdToMessageId.put(clientId, added.getMessageId());
            }
        }

        return clientIdToMessageId;
    }

    protected abstract BaseServerToClient CLIToServerToServerToClients(BaseCLIToServer cliRequest);

    protected abstract Map<Long, String> updateServerAndReturnAnswer(Map<Long, Integer> clientIdToMessageId);
}