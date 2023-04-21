package com.company.services.jpa;

import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.statuses.ExecutionStatus;
import com.company.entities.ExecutionResult;
import com.company.repositories.ExecutionResultRepository;
import com.company.services.ClientManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExecutionResultJpaServiceTest {

    @Mock
    private ClientManagerService clientManagerService;

    @Mock
    private ExecutionResultRepository executionResultRepository;

    private ExecutionResultJpaService executionResultJpaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        executionResultJpaService = new ExecutionResultJpaService(clientManagerService, executionResultRepository);
    }

    @Test
    void save() {
        // given
        Long clientId = 1234L;
        Integer messageId = 1;
        ExecutionResult savedResult = new ExecutionResult();
        savedResult.setMessageId(messageId);
        savedResult.setStatus(ExecutionStatus.SENT);
        when(executionResultRepository.saveAndSetMessageId(any())).thenReturn(savedResult);

        // when
        ExecutionResult result = executionResultJpaService.save(clientId);

        // then
        assertNotNull(result);
        assertEquals(savedResult, result);
        assertEquals(ExecutionStatus.SENT, result.getStatus());
        verify(clientManagerService, times(1)).addExecutionResult(eq(clientId), any(ExecutionResult.class));
        verify(executionResultRepository, times(1)).saveAndSetMessageId(any(ExecutionResult.class));
    }

    @Test
    void addResult() {
        // given
        ExecutionData result = new ExecutionData();
        result.setMessageId(1);
        result.setStatus(ExecutionStatus.RECEIVED);
        result.setResult("base64encodedResult");

        ExecutionResult executionResult = new ExecutionResult();
        executionResult.setMessageId(result.getMessageId());
        executionResult.setStatus(result.getStatus());
        executionResult.setBase64Result(result.getResult());

        when(executionResultRepository.save(any(ExecutionResult.class))).thenReturn(1);

        // when
        executionResultJpaService.addResult(result);

        // then
        verify(executionResultRepository, times(1)).save(executionResult);
    }

    @Test
    void getResultsById() {
        // given
        Integer payloadId = 1;

        ExecutionResult result1 = new ExecutionResult();
        result1.setId(payloadId);
        result1.setMessageId(payloadId);
        result1.setStatus(ExecutionStatus.SENT);

        ExecutionResult result2 = new ExecutionResult();
        result2.setId(2);
        result2.setMessageId(payloadId);
        result2.setStatus(ExecutionStatus.RECEIVED);

        List<ExecutionResult> expectedResults = Arrays.asList(result1, result2);
        when(executionResultRepository.findAllByPayloadId(payloadId)).thenReturn(expectedResults);

        // when
        Optional<List<ExecutionData>> actualResults = executionResultJpaService.getResultsById(payloadId);

        // then
        assertNotNull(actualResults);
        assertTrue(actualResults.isPresent());
        assertEquals(2, actualResults.get().size());
    }

    @Test
    void getAllResultsIds() {
        // given
        List<Integer> expectedIds = Arrays.asList(1, 2, 3);
        when(executionResultRepository.findAllIds()).thenReturn(expectedIds);

        // when
        List<Integer> resultIds = executionResultJpaService.getAllResultsIds();

        // then
        assertNotNull(resultIds);
        assertEquals(expectedIds.size(), resultIds.size());
        assertTrue(resultIds.containsAll(expectedIds));
        verify(executionResultRepository, times(1)).findAllIds();
    }
}