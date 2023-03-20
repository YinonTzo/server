package com.company.plugins;

import com.company.commands.BaseRemoveCommand;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;

/**
 * This class is a concrete implementation of the abstract "BaseRemoveCommand" class,
 * specifically for the "RemoveClient" command.
 * It provides the necessary parameters and methods to handle the removal of clients from a server.
 */
public class RemoveClientCommand extends BaseRemoveCommand {

    public static final String COMMAND_NAME = "RemoveClient";

    public RemoveClientCommand(ClientManagerService clientManagerService,
                               ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);
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