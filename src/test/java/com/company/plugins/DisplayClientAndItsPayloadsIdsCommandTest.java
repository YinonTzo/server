package com.company.plugins;

import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.ClientIdAndPayloadsIds;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DisplayClientAndItsPayloadsIdsCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    private Command displayClientAndItsPayloadsIdsCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayClientAndItsPayloadsIdsCommand
                = new DisplayClientAndItsPayloadsIdsCommand(mockClientManagerService, mockExecutionResultService);
    }

    @Test
    public void executeWithSingleClientId() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME);
        cliRequest.setRequestIds(List.of(1));
        when(mockExecutionResultService.getPayloadsIdsByClientId(1)).thenReturn(Optional.of(List.of(2)));

        //when
        BaseServerToCLI actual = displayClientAndItsPayloadsIdsCommand.execute(cliRequest);

        //then
        assertEquals(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME, actual.getType());
        Map<String, String> clientAndPayloads = ((ClientIdAndPayloadsIds) actual).getClientAndPayloads();
        assertEquals(1, clientAndPayloads.size());
        assertEquals("[2]", clientAndPayloads.get("1"));
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = displayClientAndItsPayloadsIdsCommand.getCommandName();

        //then
        assertEquals(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME, commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = displayClientAndItsPayloadsIdsCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}