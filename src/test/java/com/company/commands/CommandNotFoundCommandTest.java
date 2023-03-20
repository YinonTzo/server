package com.company.commands;

import com.company.commands.constantCommands.CommandNotFoundCommand;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandNotFoundCommandTest {

    Command commandNotFoundCommand;

    @BeforeEach
    void setUp() {
        commandNotFoundCommand = new CommandNotFoundCommand();
    }

    @Test
    void execute() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType("nonExistentCommand");

        //when
        BaseServerToCLI actualResult = commandNotFoundCommand.execute(cliRequest);

        //then
        assertEquals(CommandNotFoundCommand.COMMAND_NAME, actualResult.getType());
        assertEquals(String.format(CommandNotFoundCommand.ERROR_MESSAGE, cliRequest.getType(), cliRequest.getType()),
                ((TextMessage) actualResult).getText());
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = commandNotFoundCommand.getCommandName();

        //then
        assertEquals(CommandNotFoundCommand.COMMAND_NAME, commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = commandNotFoundCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}