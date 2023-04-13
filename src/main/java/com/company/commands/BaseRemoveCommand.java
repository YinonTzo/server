package com.company.commands;

import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToClient.BaseServerToClient;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an abstract class, which implements the "Command" interface.
 * The purpose of this class is to provide a base implementation for removing clients from the server.
 */
public abstract class BaseRemoveCommand extends ServerToClientCommand {

    public static final String REMOVE_CLIENTS_MESSAGE = "Remove client successfully.";
    public static final String REMOVE_CLIENT = "RemoveClient";

    public BaseRemoveCommand(ClientManagerService clientManagerService,
                             ExecutionResultService executionResultService) {
        super(clientManagerService, executionResultService);
    }

    @Override
    protected BaseServerToClient CLIToServerToServerToClients(BaseCLIToServer cliRequest) {
        BaseServerToClient serverToClient = new BaseServerToClient();
        serverToClient.setType(convertType());
        return serverToClient;
    }

    @Override
    protected Map<Long, String> updateServerAndReturnAnswer(Map<Long, Integer> clientIdToMessageId) {
        for (Map.Entry<Long, Integer> entry : clientIdToMessageId.entrySet()) {
            Long clientId = entry.getKey();
            Integer messageId = entry.getValue();
            if (messageId != null) {
                clientManagerService.removeClient(clientId, messageId);
            }
        }

        Map<Long, String> clientIdToAck = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : clientIdToMessageId.entrySet()) {
            if (entry.getValue() != null) {
                clientIdToAck.put(entry.getKey(), REMOVE_CLIENTS_MESSAGE);
            } else {
                clientIdToAck.put(entry.getKey(), null);
            }
        }
        return clientIdToAck;
    }

    private String convertType() {
        return REMOVE_CLIENT;
    }
}