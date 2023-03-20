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
    void testExecute() throws Exception {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer();
        cliRequest.setType("TestType"); //-----------------------
        cliRequest.setRequestIds(Arrays.asList(1, 2, 3));

        // Mock behavior of client manager service
        Server.ClientHandler clientHandler1 = mock(Server.ClientHandler.class);
        when(clientHandler1.isAvailable()).thenReturn(true);
        when(clientHandler1.getClientId()).thenReturn(1);
        when(mockClientManagerService.getClient(1)).thenReturn(clientHandler1);

        Server.ClientHandler clientHandler2 = mock(Server.ClientHandler.class);
        when(clientHandler2.isAvailable()).thenReturn(false);
        when(clientHandler2.getClientId()).thenReturn(2);
        when(mockClientManagerService.getClient(2)).thenReturn(clientHandler2);

        Server.ClientHandler clientHandler3 = mock(Server.ClientHandler.class);
        when(clientHandler3.isAvailable()).thenReturn(true);
        when(clientHandler3.getClientId()).thenReturn(3);
        when(mockClientManagerService.getClient(3)).thenReturn(clientHandler3);

        // Mock behavior of execution result service
        ExecutionData executionData1 = mock(ExecutionData.class);
        when(mockExecutionResultService.save(clientHandler1)).thenReturn(executionData1);
        when(executionData1.getMessageId()).thenReturn(123);

        ExecutionData executionData3 = mock(ExecutionData.class);
        when(mockExecutionResultService.save(clientHandler3)).thenReturn(executionData3);
        when(executionData3.getMessageId()).thenReturn(456);

        // Set up expected results
        TextMessage expectedTextMessage = new TextMessage();
        expectedTextMessage.setType("TestType");

        //when
        BaseServerToCLI result = removeClientCommand.execute(cliRequest);

        //then
        assertEquals(expectedTextMessage.getType(), result.getType());
        verify(clientHandler1).setUnavailable();
        verify(clientHandler1).setRemoveMessageId(123);
        verify(clientHandler3).setUnavailable();
        verify(clientHandler3).setRemoveMessageId(456);
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