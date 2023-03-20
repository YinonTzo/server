package com.company.plugins;

import com.company.commands.ServerToClientCommand;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.common.messages.serverToClient.PayloadServerToClient;
import com.company.server.Server;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * The SendPayloadCommand class is a concrete implementation of the ServerToClientCommand abstract class,
 * responsible for handling client requests to send payloads to other clients.
 */
public class SendPayloadCommand extends ServerToClientCommand {

    public static final String SENT_PAYLOAD_MESSAGE = "Sent payload numbers %s to clients: %s";
    public static final String COMMAND_NAME = "SendPayloadCommand";

    public SendPayloadCommand(ClientManagerService clientManagerService,
                              ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);
    }

    @Override
    protected BaseServerToClient CLIToServerToServerToClients(BaseCLIToServer cliRequest) {
        PayloadCLIToServer cliRequestAsPayloadMessage = (PayloadCLIToServer) cliRequest;

        PayloadServerToClient payloadMessage = new PayloadServerToClient();
        payloadMessage.setType(cliRequest.getType());
        payloadMessage.setPayload(cliRequestAsPayloadMessage.getPayload());
        payloadMessage.setArguments(cliRequestAsPayloadMessage.getArguments());
        return payloadMessage;
    }

    @Override
    protected String updateServerAndReturnAnswer(Map<Server.ClientHandler, Integer> clientToMessageId) {
        return String.format(SENT_PAYLOAD_MESSAGE, clientToMessageId.values(),
                clientToMessageId.keySet()
                        .stream()
                        .map(Server.ClientHandler::getClientId)
                        .collect(Collectors.toList()));
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public boolean isKeepRunningCommand() {
        return true;
    }
}