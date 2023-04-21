package com.company.services.jpa;

import com.company.common.messages.clientToServer.ExecutionData;
import com.company.entities.ExecutionResult;
import com.company.repositories.ExecutionResultRepository;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implements the "ExecutionResultService" interface. It provides methods to save, add, retrieve execution results.
 */

@AllArgsConstructor
@Slf4j
public class ExecutionResultJpaService implements ExecutionResultService {

    private final ClientManagerService clientManagerService;
    private final ExecutionResultRepository executionResultRepository;

    @Override
    public ExecutionResult save(Long clientId) {
        ExecutionResult updatedResult = executionResultRepository.saveAndSetMessageId(new ExecutionResult());
        clientManagerService.addExecutionResult(clientId, updatedResult);
        return updatedResult;
    }

    @Override
    public void addResult(ExecutionData result) {
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.setMessageId(result.getMessageId());
        executionResult.setStatus(result.getStatus());
        executionResult.setBase64Result(result.getResult());
        executionResultRepository.save(executionResult);
    }

    @Override
    public Optional<List<ExecutionData>> getResultsById(int payloadId) {
        List<ExecutionResult> executionResults = executionResultRepository.findAllByPayloadId(payloadId);
        if (executionResults == null) {
            return Optional.empty();
        }

        List<ExecutionData> executionDataList = new ArrayList<>();
        for (ExecutionResult executionResult : executionResults) {
            //convert to executionData
            ExecutionData executionData = new ExecutionData(executionResult.getMessageId());
            executionData.setStatus(executionResult.getStatus());
            executionData.setResult(executionResult.getBase64Result());
            executionDataList.add(executionData);
        }

        return Optional.of(executionDataList);
    }

    @Override
    public List<Integer> getAllResultsIds() {
        return executionResultRepository.findAllIds();
    }
}