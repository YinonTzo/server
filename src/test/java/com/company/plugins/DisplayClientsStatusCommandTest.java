package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientsAndStatuses;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DisplayClientsStatusCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    Command displayClientsStatusCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayClientsStatusCommand = new DisplayClientsStatusCommand(mockClientManagerService, mockExecutionResultService);
    }

    @Test
    void execute() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType(DisplayClientsStatusCommand.COMMAND_NAME);

        Map<Integer, ClientAndServerStatus> clientAndServerStatusMap = new HashMap<>();
        clientAndServerStatusMap.put(1, ClientAndServerStatus.AVAILABLE);
        clientAndServerStatusMap.put(2, ClientAndServerStatus.UNAVAILABLE);
        clientAndServerStatusMap.put(3, ClientAndServerStatus.AVAILABLE);
        when(mockClientManagerService.getAllClientsAndStatuses()).thenReturn(clientAndServerStatusMap);

        //when
        BaseServerToCLI actual = displayClientsStatusCommand.execute(cliRequest);

        //then
        assertEquals(DisplayClientsStatusCommand.COMMAND_NAME, actual.getType());
        assertEquals(clientAndServerStatusMap, ((ClientsAndStatuses) actual).getClientsAndStatuses());
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = displayClientsStatusCommand.getCommandName();

        //then
        assertEquals(DisplayClientsStatusCommand.COMMAND_NAME, commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = displayClientsStatusCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}