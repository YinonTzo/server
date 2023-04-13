package com.company.plugins;

import com.company.commands.Command;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;

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