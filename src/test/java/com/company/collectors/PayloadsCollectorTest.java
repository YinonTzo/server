package com.company.collectors;

import com.company.services.ExecutionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PayloadsCollectorTest {

    @Mock
    private ExecutionResultService mockExecutionResultService;

    private PayloadsCollector payloadsCollector;

    @BeforeEach
    void setUp() {
        mockExecutionResultService = mock(ExecutionResultService.class);
        payloadsCollector = new PayloadsCollector(mockExecutionResultService);
    }

    @Test
    void testCollectWithBroadcast() {
        // given
        List<Integer> wantedIds = Arrays.asList(-1);
        List<Integer> expectedPayloadsIds = new ArrayList<>();
        expectedPayloadsIds.add(1);
        expectedPayloadsIds.add(2);
        expectedPayloadsIds.add(3);
        expectedPayloadsIds.add(4);
        expectedPayloadsIds.add(5);

        when(mockExecutionResultService.getAllResultsIds()).thenReturn(expectedPayloadsIds);

        // when
        List<Integer> collectedPayloadsIds = payloadsCollector.collect(wantedIds);

        // then
        assertEquals(expectedPayloadsIds, collectedPayloadsIds);
    }

    @Test
    void testCollectWithSpecificClients() {
        // given
        List<Integer> wantedIds = Arrays.asList(1, 2, 3, 4, 5);

        // when
        List<Integer> collectedPayloadsIds = payloadsCollector.collect(wantedIds);

        // then
        assertEquals(wantedIds, collectedPayloadsIds);
    }
}