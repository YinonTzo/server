package com.company.plugins;

import com.company.commands.Command;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DisplayCommandsResultCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    Command displayCommandsResultCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayCommandsResultCommand = new DisplayCommandsResultCommand(mockClientManagerService,
                mockExecutionResultService);
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
        payloadIdToResult.put(messageId2,  Arrays.asList(new ExecutionData(messageId2)));
        payloadIdToResult.put(messageId3,  Arrays.asList(new ExecutionData(messageId3)));

        when(mockExecutionResultService.getCommandResults(messageId1)).thenReturn(payloadIdToResult.get(messageId1));
        when(mockExecutionResultService.getCommandResults(messageId2)).thenReturn(payloadIdToResult.get(messageId2));
        when(mockExecutionResultService.getCommandResults(messageId3)).thenReturn(payloadIdToResult.get(messageId3));

        // when
        BaseServerToCLI actual = displayCommandsResultCommand.execute(cliRequest);

        // then
        assertEquals(cliRequest.getType(), actual.getType());
        CommandResults commandResults = (CommandResults) actual;
        assertEquals(3, commandResults.getPayloadIdToResult().size());
        assertEquals(payloadIdToResult, commandResults.getPayloadIdToResult());
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = displayCommandsResultCommand.getCommandName();

        //then
        assertEquals(DisplayCommandsResultCommand.COMMAND_NAME, commandName);
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