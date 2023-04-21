package com.company.plugins;

import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.SendPayload;
import com.company.entities.ExecutionResult;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.company.plugins.SendPayloadCommand.SENT_PAYLOAD_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SendPayloadCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    private SendPayloadCommand sendPayloadCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sendPayloadCommand = new SendPayloadCommand(mockClientManagerService, mockExecutionResultService);
    }

    @Test
    void execute() {
        // given
        Long clientId = 123L;

        PayloadCLIToServer cliRequest = new PayloadCLIToServer("some type");
        cliRequest.setRequestIds(List.of(clientId.intValue()));
        cliRequest.setPayload("payload".getBytes(StandardCharsets.UTF_8));
        cliRequest.setArguments("arg1 arg2");

        when(mockClientManagerService.isConnected(clientId)).thenReturn(true);
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.setMessageId(1);
        when(mockExecutionResultService.save(clientId)).thenReturn(executionResult);

        // when
        BaseServerToCLI actual = sendPayloadCommand.execute(cliRequest);

        // then
        SendPayload sendPayload = (SendPayload) actual;
        assertNotNull(sendPayload);
        assertNotNull(sendPayload.getClientIdToAck());
        assertEquals(1, sendPayload.getClientIdToAck().size());
        assertTrue(sendPayload.getClientIdToAck().containsKey(clientId));
        assertNotNull(sendPayload.getClientIdToAck().get(clientId));
    }

    @Test
    void executeWithMissingClient() {
        // given
        Long clientId1 = 123L;
        Long clientId2 = 1234L;

        PayloadCLIToServer cliRequest = new PayloadCLIToServer("some type");
        cliRequest.setRequestIds(List.of(clientId1.intValue(), clientId2.intValue()));
        cliRequest.setPayload("payload".getBytes(StandardCharsets.UTF_8));
        cliRequest.setArguments("arg1 arg2");

        when(mockClientManagerService.isConnected(clientId1)).thenReturn(true);
        when(mockClientManagerService.isConnected(clientId2)).thenReturn(false);
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.setMessageId(1);
        when(mockExecutionResultService.save(clientId1)).thenReturn(executionResult);

        // when
        BaseServerToCLI actual = sendPayloadCommand.execute(cliRequest);

        // then
        SendPayload sendPayload = (SendPayload) actual;
        assertNotNull(sendPayload);
        assertNotNull(sendPayload.getClientIdToAck());
        assertEquals(2, sendPayload.getClientIdToAck().size());
        assertTrue(sendPayload.getClientIdToAck().containsKey(clientId1));
        assertTrue(sendPayload.getClientIdToAck().containsKey(clientId2));
        assertNotNull(sendPayload.getClientIdToAck().get(clientId1));
        assertNull(sendPayload.getClientIdToAck().get(clientId2));
    }

    @Test
    void getCommandName() {
        //no given

        //when
        String commandName = sendPayloadCommand.getCommandName();

        //then
        assertEquals(SendPayloadCommand.COMMAND_NAME, commandName);
    }

    @Test
    void isKeepRunningCommand() {
        //no given

        //when
        boolean isRunning = sendPayloadCommand.isKeepRunningCommand();

        //then
        assertTrue(isRunning);
    }
}