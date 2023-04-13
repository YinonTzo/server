package com.company.services.map;

import com.company.common.messages.clientToServer.ExecutionData;
import com.company.entities.ExecutionResult;
import com.company.common.statuses.ExecutionStatus;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import lombok.ToString;

import java.util.*;

@ToString
public class ExecutionResults implements ExecutionResultService {

    private final ClientManagerService clientManagerService;
    private final Map<Integer, List<ExecutionResult>> payloadIdToResults = new HashMap<>(); //repository
    private Integer messageId = 0; //Should be Long, but the instructions said int.

    public ExecutionResults(ClientManagerService clientManagerService) {
        this.clientManagerService = clientManagerService;
    }

    @Override
    public ExecutionResult save(Long clientId) {
        ExecutionResult newExecutionResult = new ExecutionResult();
        newExecutionResult.setId(messageId);
        newExecutionResult.setMessageId(messageId);
        newExecutionResult.setStatus(ExecutionStatus.SENT);
        messageId++;

        payloadIdToResults.putIfAbsent(newExecutionResult.getMessageId(), new ArrayList<>());
        payloadIdToResults.get(newExecutionResult.getMessageId()).add(newExecutionResult);

        clientManagerService.addExecutionResult(clientId, newExecutionResult);
        return newExecutionResult;
    }

    @Override
    public void addResult(ExecutionData result) {
        //convert to ExecutionResult
        ExecutionResult executionResult = new ExecutionResult();
        executionResult.setId(messageId++);
        executionResult.setMessageId(result.getMessageId());
        executionResult.setStatus(result.getStatus());
        executionResult.setBase64Result(result.getResult());

        //save
        payloadIdToResults.get(result.getMessageId()).add(executionResult);
    }

    @Override
    public Optional<List<ExecutionData>> getResults(int commandResultId) {
        List<ExecutionResult> executionResults = payloadIdToResults.get(commandResultId);

        if (executionResults == null) {
            return Optional.empty();
        }

        List<ExecutionData> executionDataList = new ArrayList<>();

        for (ExecutionResult executionResult : executionResults) {
            ExecutionData executionData = new ExecutionData(executionResult.getMessageId());
            executionData.setStatus(executionResult.getStatus());
            executionData.setResult(executionResult.getBase64Result());
            executionDataList.add(executionData);
        }
        return Optional.of(executionDataList);
    }

    @Override
    public List<Integer> getAllResultsIds() {
        return (List<Integer>) payloadIdToResults.keySet();
    }
}