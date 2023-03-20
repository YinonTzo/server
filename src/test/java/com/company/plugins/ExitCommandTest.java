package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.TextMessage;
import com.company.server.Server;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ExitCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    Command exitCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exitCommand = new ExitCommand(mockClientManagerService, mockExecutionResultService);
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = exitCommand.getCommandName();

        //then
        assertEquals("ExitCommand", commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = exitCommand.isKeepRunningCommand();

        //then
        assertFalse(isRunning);
    }
}