package com.company.commands;

import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;

/**
 * The "CLIToServerCommand" class is an abstract base class that implements the "Command" interface.
 * It is designed for commands that are intended to retrieve data about clients or payloads from the server,
 * rather than send messages to remote clients.
 */
public abstract class CLIToServerCommand implements Command {

    protected final ClientManagerService clientManagerService;
    protected final ExecutionResultService executionResultService;

    public CLIToServerCommand(ClientManagerService clientManagerService,
                              ExecutionResultService executionResultService) {
        this.clientManagerService = clientManagerService;
        this.executionResultService = executionResultService;
    }

    @Override
    public boolean isKeepRunningCommand() {
        return true;
    }
}