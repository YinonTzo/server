package com.company.services.jpa;

import com.company.common.messages.serverToClient.BaseServerToClient;
import com.company.common.statuses.ClientAndServerStatus;
import com.company.entities.Client;
import com.company.entities.ExecutionResult;
import com.company.repositories.ClientRepository;
import com.company.server.Server;
import com.company.services.map.ClientHandlers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientJpaServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientHandlers clientHandlers;

    private ClientJpaService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientJpaService(clientRepository, clientHandlers);
    }

    @Test
    void addClient() {
        // given
        Server.ClientHandler client = mock(Server.ClientHandler.class);
        Client newClient = new Client();
        Long newClientId = 1L;
        newClient.setId(newClientId);
        when(clientRepository.save(any(Client.class))).thenReturn(newClient);

        // when
        Long actualId = clientService.addClient(client);

        // then
        verify(clientHandlers).add(newClientId, client);
        assertEquals(newClientId, actualId);
    }

    @Test
    void removeClient() {
        // given
        Long clientId = 1L;
        Integer removeMessageId = 2;
        Server.ClientHandler clientHandler = mock(Server.ClientHandler.class);
        when(clientHandlers.getClient(clientId)).thenReturn(clientHandler);

        // when
        clientService.removeClient(clientId, removeMessageId);

        // then
        verify(clientHandlers).getClient(clientId);
        verify(clientHandler).setRemoveMessageId(removeMessageId);
        verify(clientHandlers).remove(clientId);
    }

    @Test
    void getClientWhenClientExists() {
        // given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        //when
        Optional<Client> result = clientService.getClient(clientId);

        //then
        assertTrue(result.isPresent());
        assertEquals(result.get(), client);
    }

    @Test
    void getClientWhenClientDoesNotExist() {
        // given
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        //when
        Optional<Client> result = clientService.getClient(clientId);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void sendMessage() {
        // given
        Long clientId = 1L;
        BaseServerToClient message = new BaseServerToClient();

        // when
        try {
            clientService.sendMessage(clientId, message);

            // then
            verify(clientHandlers).sendMessage(clientId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllClientsAndStatuses() {
        // given
        Long clientId1 = 1L;
        Long clientId2 = 2L;
        Long clientId3 = 3L;

        Client client1 = new Client();
        client1.setId(clientId1);
        Client client2 = new Client();
        client2.setId(clientId2);
        Client client3 = new Client();
        client3.setId(clientId3);

        when(clientRepository.findAllIds()).thenReturn(List.of(clientId1, clientId2, clientId3));
        when(clientHandlers.contains(clientId1)).thenReturn(true);
        when(clientHandlers.contains(clientId2)).thenReturn(false);
        when(clientHandlers.contains(clientId3)).thenReturn(true);

        // when
        Map<Long, ClientAndServerStatus> clientsAndStatuses = clientService.getAllClientsAndStatuses();

        // then
        assertEquals(3, clientsAndStatuses.size());

        ClientAndServerStatus client1Status = clientsAndStatuses.get(clientId1);
        assertEquals(ClientAndServerStatus.AVAILABLE, client1Status);

        ClientAndServerStatus client2Status = clientsAndStatuses.get(clientId2);
        assertEquals(ClientAndServerStatus.UNAVAILABLE, client2Status);

        ClientAndServerStatus client3Status = clientsAndStatuses.get(clientId3);
        assertEquals(ClientAndServerStatus.AVAILABLE, client3Status);
    }

    @Test
    void getStatus() {
        // given
        Long clientId1 = 1L;
        Long clientId2 = 2L;
        Long clientId3 = 3L;
        when(clientHandlers.contains(clientId1)).thenReturn(true);
        when(clientHandlers.contains(clientId2)).thenReturn(false);
        when(clientHandlers.contains(clientId3)).thenReturn(true);
        when(clientRepository.findAllIds()).thenReturn(List.of(clientId1, clientId2, clientId3));

        // when
        ClientAndServerStatus status1 = clientService.getStatus(clientId1);
        ClientAndServerStatus status2 = clientService.getStatus(clientId2);
        ClientAndServerStatus status3 = clientService.getStatus(clientId3);

        // then
        assertEquals(ClientAndServerStatus.AVAILABLE, status1);
        assertEquals(ClientAndServerStatus.UNAVAILABLE, status2);
        assertEquals(ClientAndServerStatus.AVAILABLE, status3);
    }

    @Test
    void addExecutionResult() {
        // given
        Long clientId = 1L;
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.setMessageId(1);

        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        clientService.addExecutionResult(clientId, executionResult);

        // then
        verify(clientRepository).findById(clientId);
        assertEquals(1, client.getResults().size());
        assertEquals(executionResult, client.getResults().get(0));
    }

    @Test
    void getAllClientsIds() {
        // given
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        when(clientRepository.findAllIds()).thenReturn(expectedIds);

        // when
        List<Long> actualIds = clientService.getAllClientsIds();

        // then
        assertEquals(expectedIds, actualIds);
    }

    @Test
    void getPayloadsIdsByClientIdWithExistingClient() {
        // given
        Long clientId = 1L;
        Client client = new Client();
        ExecutionResult result1 = ExecutionResult.builder().id(1).messageId(1).build();
        ExecutionResult result2 = ExecutionResult.builder().id(2).messageId(2).build();
        client.setResults(Arrays.asList(result1, result2));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // when
        Optional<List<Integer>> optionalResultIds = clientService.getPayloadsIdsByClientId(clientId);

        // then
        assertTrue(optionalResultIds.isPresent());
        assertEquals(Arrays.asList(1, 2), optionalResultIds.get());
    }

    @Test
    void getPayloadsIdsByClientIdWithNonExistingClient() {
        // given
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // when
        Optional<List<Integer>> optionalResultIds = clientService.getPayloadsIdsByClientId(clientId);

        // then
        assertFalse(optionalResultIds.isPresent());
    }

    @Test
    void isConnectedWhenTrue() {
        // given
        Long clientId = 1L;
        when(clientHandlers.contains(clientId)).thenReturn(true);

        // when
        boolean isConnected = clientService.isConnected(clientId);

        // then
        assertTrue(isConnected);
    }

    @Test
    void isConnectedWhenFalse() {
        // given
        Long clientId = 1L;
        when(clientHandlers.contains(clientId)).thenReturn(false);

        // when
        boolean isConnected = clientService.isConnected(clientId);

        // then
        assertFalse(isConnected);
    }
}