package com.company.plugins;

import com.company.commands.CLIToServerCommand;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientIdAndPayloadsIds;
import com.company.server.Server;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The "DisplayClientAndItsPayloadsIdsCommand" class is a concrete subclass of the "CLIToServerCommand" class.
 * It is intended to retrieve data about clients and their payloads from the server,
 * and returns the results as a "ClientIdAndPayloadsIds" object.
 */
public class DisplayClientAndItsPayloadsIdsCommand extends CLIToServerCommand {
    public static final String COMMAND_NAME = "DisplayClientAndItsPayloadsIds";

    public DisplayClientAndItsPayloadsIdsCommand(ClientManagerService clientManagerService,
                                                 ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);
    }

    @Override
    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        Map<String, String> clientAndPayloads = new HashMap<>();

        List<Integer> wantedClientsIds = collectWantedClients(cliRequest.getRequestIds());

        for (Integer wantedClientId : wantedClientsIds) {

            Optional<List<Integer>> optionalPayloadsIdsByClientId =
                    executionResultService.getPayloadsIdsByClientId(wantedClientId);

            String payloadsIds = optionalPayloadsIdsByClientId.map(Object::toString).orElse("");

            clientAndPayloads.put(wantedClientId.toString(), payloadsIds);
        }

        ClientIdAndPayloadsIds result = new ClientIdAndPayloadsIds();
        result.setType(cliRequest.getType());
        result.setClientAndPayloads(clientAndPayloads);
        return result;
    }

    private List<Integer> collectWantedClients(List<Integer> wantedClientsIds) {
        if (wantedClientsIds.contains(-1)) { //broadcast
            return clientManagerService.getAllClients().stream()
                    .map(Server.ClientHandler::getClientId)
                    .collect(Collectors.toList());
        }

        return wantedClientsIds;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
