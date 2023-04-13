package com.company.plugins;

import com.company.collectors.PayloadsCollector;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.CommandResults;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class DisplayResultsCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    @Mock
    private PayloadsCollector payloadsCollector;

    DisplayResultsCommand displayCommandsResultCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayCommandsResultCommand = new DisplayResultsCommand(mockClientManagerService, mockExecutionResultService);
        displayCommandsResultCommand.setPayloadsCollector(payloadsCollector);
    }

    @Test
    void execute() {
        // given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType("command_name");
        int messageId1 = 1;
        int messageId2 = 2;
        int messageId3 = 3;
        cliRequest.setRequestIds(Arrays.asList(messageId1, messageId2, messageId3));

        Map<Integer, List<ExecutionData>> payloadIdToResult = new HashMap<>();
        payloadIdToResult.put(messageId1, Arrays.asList(new ExecutionData(messageId1)));
        payloadIdToResult.put(messageId2, Arrays.asList(new ExecutionData(messageId2)));
        payloadIdToResult.put(messageId3, Arrays.asList(new ExecutionData(messageId3)));

        when(payloadsCollector.collect(anyList())).thenReturn(Arrays.asList(messageId1, messageId2, messageId3));

        when(mockExecutionResultService.getResults(messageId1)).thenReturn(Optional.of(payloadIdToResult.get(messageId1)));
        when(mockExecutionResultService.getResults(messageId2)).thenReturn(Optional.of(payloadIdToResult.get(messageId2)));
        when(mockExecutionResultService.getResults(messageId3)).thenReturn(Optional.of(payloadIdToResult.get(messageId3)));

        // when
        BaseServerToCLI actual = displayCommandsResultCommand.execute(cliRequest);

        // then
        assertEquals(CommandResults.class, actual.getClass());
        CommandResults commandResults = (CommandResults) actual;
        assertEquals(cliRequest.getType(), commandResults.getType());
        assertEquals(payloadIdToResult, commandResults.getPayloadIdToResult());
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = displayCommandsResultCommand.getCommandName();

        //then
        assertEquals(DisplayResultsCommand.COMMAND_NAME, commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = displayCommandsResultCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}