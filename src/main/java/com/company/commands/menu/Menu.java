package com.company.commands.menu;

import com.company.commands.Command;
import com.company.commands.constantCommands.CommandNotFoundCommand;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;

import java.util.Map;

/**
 * The "Menu" class serves as an interface between a command-line interface (CLI) and a server.
 * It allows the CLI to send various types of requests to the server,
 * such as commands to execute on remote clients or
 * requests for information about remote clients stored in the server's databases.
 */
public class Menu {
    private final Map<String, Command> commands;
    private final CommandNotFoundCommand commandNotFoundCommand = new CommandNotFoundCommand();
    boolean run = true;

    public Menu(Map<String, Command> commands) {
        this.commands = commands;
    }

    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        Command requestedCommand = commands.get(cliRequest.getType());
        if (requestedCommand == null) {
            return commandNotFoundCommand.execute(cliRequest);
        }

        run = requestedCommand.isKeepRunningCommand();

        return requestedCommand.execute(cliRequest);
    }

    public boolean isRun() {
        return run;
    }
}
