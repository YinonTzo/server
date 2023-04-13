package com.company.plugins;

import com.company.commands.BaseRemoveCommand;
import com.company.commands.Command;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.SendPayload;
import com.company.entities.ExecutionResult;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemoveClientCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    Command removeClientCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        removeClientCommand = new RemoveClientCommand(mockClientManagerService, mockExecutionResultService);
    }

    @Test
    void execute() {
        // given
        Integer clientId = 123;
        Integer messageId = 456;

        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType("someType");
        cliRequest.setRequestIds(Arrays.asList(clientId));
        ExecutionResult removeMessage = new ExecutionResult();
        removeMessage.setMessageId(messageId);

        when(mockClientManagerService.isConnected(clientId.longValue())).thenReturn(true);
        when(mockExecutionResultService.save(any(Long.class))).thenReturn(removeMessage);

        // when
        BaseServerToCLI actual = removeClientCommand.execute(cliRequest);

        // then
        verify(mockClientManagerService).removeClient(clientId.longValue(), messageId);
        Map<Long, String> expectedClientIdToAck = new HashMap<>();
        expectedClientIdToAck.put(clientId.longValue(), BaseRemoveCommand.REMOVE_CLIENTS_MESSAGE);
        SendPayload sendPayload = (SendPayload) actual;
        assertEquals(expectedClientIdToAck, sendPayload.getClientIdToAck());
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = removeClientCommand.getCommandName();

        //then
        assertEquals(RemoveClientCommand.COMMAND_NAME, commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = removeClientCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}