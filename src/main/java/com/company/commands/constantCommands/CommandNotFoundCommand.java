package com.company.commands.constantCommands;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.TextMessage;

/**
 * The "CommandNotFoundCommand" class is a Java class that implements the Command interface.
 * It is designed to handle a scenario where a command requested by a client through the Command Line Interface (CLI)
 * is not found on the server.
 */
public class CommandNotFoundCommand implements Command {

    public static final String ERROR_MESSAGE = "CLI sent %s class, but in the server there is no %s command." +
            "Please read again the README and check how to add new commands.";
    public static final String COMMAND_NAME = "CommandNotFoundCommand";

    @Override
    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        TextMessage errorMessage = new TextMessage();
        errorMessage.setType(getCommandName());
        errorMessage.setText(String.format(ERROR_MESSAGE, cliRequest.getType(), cliRequest.getType()));
        return errorMessage;
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
