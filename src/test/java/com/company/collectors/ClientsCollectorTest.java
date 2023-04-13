package com.company.collectors;

import com.company.services.ClientManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ClientsCollectorTest {

    @Mock
    private ClientManagerService mockClientManagerService;

    private ClientsCollector clientsCollector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientsCollector = new ClientsCollector(mockClientManagerService);
    }

    @Test
    void testCollectWithBroadcast() {
        // given
        List<Integer> wantedClientsIds = Collections.singletonList(-1);
        List<Long> allClientsIds = Arrays.asList(1L, 2L, 3L);
        when(mockClientManagerService.getAllClientsIds()).thenReturn(allClientsIds);

        // when
        List<Long> result = clientsCollector.collect(wantedClientsIds);

        // then
        assertEquals(allClientsIds, result);
    }

    @Test
    void testCollectWithSpecificClients() {
        // given
        List<Integer> wantedClientsIds = Arrays.asList(1, 3, 5);
        List<Long> expected = Arrays.asList(1L, 3L, 5L);

        // when
        List<Long> result = clientsCollector.collect(wantedClientsIds);

        // then
        assertEquals(expected, result);
    }
}