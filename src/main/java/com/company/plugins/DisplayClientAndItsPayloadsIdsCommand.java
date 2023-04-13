package com.company.plugins;

import com.company.commands.CLIToServerCommand;
import com.company.collectors.ClientsCollector;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientIdAndPayloadsIds;

import java.util.*;

/**
 * The "DisplayClientAndItsPayloadsIdsCommand" class is a concrete subclass of the "CLIToServerCommand" class.
 * It is intended to retrieve data about clients and their payloads from the server,
 * and returns the results as a "ClientIdAndPayloadsIds" object.
 */
public class DisplayClientAndItsPayloadsIdsCommand extends CLIToServerCommand {

    public static final String COMMAND_NAME = "DisplayClientAndItsPayloadsIds";

    private ClientsCollector clientsCollector;

    public DisplayClientAndItsPayloadsIdsCommand(ClientManagerService clientManagerService,
                                                 ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);
        this.clientsCollector = new ClientsCollector(clientManagerService);
    }

    @Override
    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        Map<String, String> clientAndPayloads = new HashMap<>();

        List<Long> wantedClientsIds = clientsCollector.collect(cliRequest.getRequestIds());

        for (Long wantedClientId : wantedClientsIds) {

            Optional<List<Integer>> optionalPayloadsIdsByClientId =
                    clientManagerService.getPayloadsIdsByClientId(wantedClientId);

            String payloadsIds = optionalPayloadsIdsByClientId.map(Object::toString).orElse("");

            clientAndPayloads.put(wantedClientId.toString(), payloadsIds);
        }

        ClientIdAndPayloadsIds result = new ClientIdAndPayloadsIds();
        result.setType(cliRequest.getType());
        result.setClientAndPayloads(clientAndPayloads);
        return result;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    public void setClientsCollector(ClientsCollector clientsCollector) {
        this.clientsCollector = clientsCollector;
    }
}