package com.company.plugins;

import com.company.commands.ServerToClientCommand;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.common.messages.serverToClient.PayloadServerToClient;

import java.util.HashMap;
import java.util.Map;

/**
 * The "SendPayloadCommand" class is a concrete implementation of the "ServerToClientCommand" abstract class,
 * responsible for handling client requests to send payloads to other clients.
 */
public class SendPayloadCommand extends ServerToClientCommand {

    public static final String SENT_PAYLOAD_MESSAGE = "%s";
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
    protected Map<Long, String> updateServerAndReturnAnswer(Map<Long, Integer> clientIdToMessageId) {
        Map<Long, String> clientIdToAck = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : clientIdToMessageId.entrySet()) {
            if (entry.getValue() != null) {
                clientIdToAck.put(entry.getKey(), String.format(SENT_PAYLOAD_MESSAGE, entry.getValue()));
            } else {
                clientIdToAck.put(entry.getKey(), null);
            }
        }
        return clientIdToAck;
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