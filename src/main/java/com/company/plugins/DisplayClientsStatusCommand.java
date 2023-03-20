package com.company.plugins;

import com.company.commands.CLIToServerCommand;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientsAndStatuses;

/**
 * The "DisplayClientAndItsPayloadsIdsCommand" class is a concrete subclass of the CLIToServerCommand class.
 * It is intended to retrieve data about clients and their statuses from the server,
 * and returns the results as a "ClientsAndStatuses" object.
 */
public class DisplayClientsStatusCommand extends CLIToServerCommand {

    public static final String COMMAND_NAME = "DisplayClientsStatus";

    public DisplayClientsStatusCommand(ClientManagerService clientManagerService,
                                       ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);
    }

    @Override
    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        ClientsAndStatuses clientsAndStatuses = new ClientsAndStatuses();

        clientsAndStatuses.setType(cliRequest.getType());
        clientsAndStatuses.setClientsAndStatuses(clientManagerService.getAllClientsAndStatuses());

        return clientsAndStatuses;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
