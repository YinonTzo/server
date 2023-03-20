package com.company.commands;

import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.server.Server;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is an abstract class, which implements the "Command" interface.
 * The purpose of this class is to provide a base implementation for removing clients from the server.
 */
public abstract class BaseRemoveCommand extends ServerToClientCommand {

    public static final String REMOVE_CLIENTS_MESSAGE = "Remove clients %s successfully.";
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
    protected String updateServerAndReturnAnswer(Map<Server.ClientHandler, Integer> clientToMessageId) {
        for (Map.Entry<Server.ClientHandler, Integer> entry : clientToMessageId.entrySet()) {
            Server.ClientHandler client = entry.getKey();
            Integer messageId = entry.getValue();

            client.setUnavailable();
            client.setRemoveMessageId(messageId);
        }

        return String.format(REMOVE_CLIENTS_MESSAGE,
                clientToMessageId.keySet()
                        .stream()
                        .map(Server.ClientHandler::getClientId)
                        .collect(Collectors.toList())); //TODO: change the message a little bit
    }

    private String convertType() {
        return REMOVE_CLIENT;
    }
}