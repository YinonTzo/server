package com.company.plugins;

import com.company.collectors.ClientsCollector;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class DisplayClientAndItsPayloadsIdsCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    @Mock
    private ClientsCollector clientsCollector;

    private DisplayClientAndItsPayloadsIdsCommand displayClientAndItsPayloadsIdsCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayClientAndItsPayloadsIdsCommand
                = new DisplayClientAndItsPayloadsIdsCommand(mockClientManagerService, mockExecutionResultService);
        displayClientAndItsPayloadsIdsCommand.setClientsCollector(clientsCollector);
    }

    @Test
    public void executeWithSingleClientId() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME);
        Integer clientId = 1;
        cliRequest.setRequestIds(Arrays.asList(clientId));

        when(clientsCollector.collect(anyList())).thenReturn(List.of(clientId.longValue()));

        when(mockClientManagerService.getPayloadsIdsByClientId(clientId.longValue())).
                thenReturn(Optional.of(Arrays.asList(1, 2)));

        //when
        BaseServerToCLI actual = displayClientAndItsPayloadsIdsCommand.execute(cliRequest);

        //then
        assertEquals(ClientIdAndPayloadsIds.class, actual.getClass());
        assertEquals(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME, actual.getType());
        Map<String, String> clientAndPayloads = ((ClientIdAndPayloadsIds) actual).getClientAndPayloads();
        assertEquals(1, clientAndPayloads.size());
        assertEquals("[1, 2]", clientAndPayloads.get("1"));
    }

    @Test
    public void executeWithNonExistsClientId() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME);
        Integer clientId1 = 1;
        Integer clientId2 = 2;
        cliRequest.setRequestIds(Arrays.asList(clientId1, clientId2));

        when(clientsCollector.collect(anyList())).thenReturn(List.of(clientId1.longValue()));

        when(mockClientManagerService.getPayloadsIdsByClientId(clientId1.longValue())).
                thenReturn(Optional.of(Arrays.asList(1, 2)));
        when(mockClientManagerService.getPayloadsIdsByClientId(clientId2.longValue())).
                thenReturn(Optional.empty());

        //when
        BaseServerToCLI actual = displayClientAndItsPayloadsIdsCommand.execute(cliRequest);

        //then
        assertEquals(ClientIdAndPayloadsIds.class, actual.getClass());
        assertEquals(DisplayClientAndItsPayloadsIdsCommand.COMMAND_NAME, actual.getType());
        Map<String, String> clientAndPayloads = ((ClientIdAndPayloadsIds) actual).getClientAndPayloads();
        assertEquals(1, clientAndPayloads.size());
        assertEquals("[1, 2]", clientAndPayloads.get("1"));
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