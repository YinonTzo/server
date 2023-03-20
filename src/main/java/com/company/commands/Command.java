package com.company.commands;

import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

/**
 * This Java interface defines a "Command" object, which represents an operation that can be executed
 * by the Command Line Interface (CLI) to interact with the server.
 */
public interface Command {

    BaseServerToCLI execute(BaseCLIToServer cliRequest);

    String getCommandName();

    boolean isKeepRunningCommand();
}
