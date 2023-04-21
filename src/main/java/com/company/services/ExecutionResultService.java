package com.company.services;

import com.company.common.messages.clientToServer.ExecutionData;
import com.company.entities.ExecutionResult;

import java.util.List;
import java.util.Optional;

public interface ExecutionResultService {

    ExecutionResult save(Long clientID);

    void addResult(ExecutionData result);

    Optional<List<ExecutionData>> getResultsById(int commandResultId);

    List<Integer> getAllResultsIds();
}