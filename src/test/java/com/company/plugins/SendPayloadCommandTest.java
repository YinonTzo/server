package com.company.plugins;

import com.company.common.messages.CLIToServer.PayloadCLIToServer;
import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.server.Server;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SendPayloadCommandTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    @Mock
    private ExecutionResultService mockExecutionResultService;

    @Mock
    private Server.ClientHandler mockClientHandler1;

    @Mock
    private Server.ClientHandler mockClientHandler2;

    SendPayloadCommand sendPayloadCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sendPayloadCommand = new SendPayloadCommand(mockClientManagerService, mockExecutionResultService);
    }

    @Test
    void CLIToServerToServerToClients() {
        //given
        PayloadCLIToServer cliRequest = new PayloadCLIToServer("SendPayloadCommand");
        String payload = "hello world";
        cliRequest.setPayload(payload.getBytes(StandardCharsets.UTF_8));
        cliRequest.setArguments("arg1 arg2");

        //when
        BaseServerToClient actualResult = sendPayloadCommand.CLIToServerToServerToClients(cliRequest);

        //then
        assertEquals("SendPayloadCommand", ((PayloadCLIToServer) cliRequest).getType());
        assertEquals("hello world", new String(((PayloadCLIToServer) cliRequest).getPayload()));
        assertEquals("arg1 arg2", ((PayloadCLIToServer) cliRequest).getArguments());
    }

    @Test
    void updateServerAndReturnAnswer() {
        //given
        Map<Server.ClientHandler, Integer> clientToMessageId = new HashMap<>();
        clientToMessageId.put(mockClientHandler1, 1);
        clientToMessageId.put(mockClientHandler2, 2);

        when(mockClientHandler1.getClientId()).thenReturn(1);
        when(mockClientHandler2.getClientId()).thenReturn(2);

        //when
        String result = sendPayloadCommand.updateServerAndReturnAnswer(clientToMessageId);

        //then
        assertEquals("Sent payload numbers [1, 2] to clients: [1, 2]", result);
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