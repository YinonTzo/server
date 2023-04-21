package com.company.repositories;

import com.company.common.statuses.ExecutionStatus;
import com.company.entities.ExecutionResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionResultRepositoryTest {

    private EntityManagerFactory emf;
    private ExecutionResultRepository executionResultRepository;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");

        executionResultRepository = new ExecutionResultRepository(emf);
    }

    @AfterEach
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void save() {
        // given
        ExecutionResult executionResult = new ExecutionResult();

        // when
        Integer messageId = executionResultRepository.save(executionResult);

        // then
        assertNotNull(messageId);
        ExecutionResult savedResult = emf.createEntityManager().find(ExecutionResult.class, messageId);
        assertEquals(executionResult, savedResult);
    }

    @Test
    void update() {
        // given
        ExecutionResult executionResult = new ExecutionResult();
        executionResultRepository.save(executionResult);

        executionResult.setStatus(ExecutionStatus.SENT);

        // when
        ExecutionResult updatedResult = executionResultRepository.update(executionResult);

        // then
        assertNotNull(updatedResult);
        assertEquals(ExecutionStatus.SENT, updatedResult.getStatus());
    }

    @Test
    void findAllByPayloadId() {
        // given
        ExecutionResult executionResult1 = new ExecutionResult();
        executionResult1.setMessageId(1);
        executionResultRepository.save(executionResult1);

        ExecutionResult executionResult2 = new ExecutionResult();
        executionResult2.setMessageId(2);
        executionResultRepository.save(executionResult2);

        ExecutionResult executionResult3 = new ExecutionResult();
        executionResult3.setMessageId(1);
        executionResultRepository.save(executionResult3);

        // when
        List<ExecutionResult> results = executionResultRepository.findAllByPayloadId(1);

        // then
        assertEquals(2, results.size());
        assertEquals(executionResult1.getId(), results.get(0).getId());
        assertEquals(executionResult3.getId(), results.get(1).getId());
    }

    @Test
    void findAllIds() {
        // given
        ExecutionResult executionResult1 = new ExecutionResult();
        ExecutionResult executionResult2 = new ExecutionResult();
        ExecutionResult executionResult3 = new ExecutionResult();

        ExecutionResult executionResult1Saved = executionResultRepository.saveAndSetMessageId(executionResult1);
        ExecutionResult executionResult2Saved = executionResultRepository.saveAndSetMessageId(executionResult2);
        ExecutionResult executionResult3Saved = executionResultRepository.saveAndSetMessageId(executionResult3);

        // when
        List<Integer> allResults = executionResultRepository.findAllIds();

        // then
        assertEquals(3, allResults.size());
        assertTrue(allResults.contains(executionResult1Saved.getMessageId()));
        assertTrue(allResults.contains(executionResult2Saved.getMessageId()));
        assertTrue(allResults.contains(executionResult3Saved.getMessageId()));
    }
}