package com.company.collectors;

import com.company.services.ExecutionResultService;

import java.util.List;

/**
 * The "PayloadsCollector" class is responsible for collecting a list of payload IDs based on a list of requested IDs.
 * The class implements the Collector interface that defines a generic method collect that takes a list of IDs
 * and returns a list of generic type T.
 */
public class PayloadsCollector implements Collector<Integer> {

    private final ExecutionResultService executionResultService;

    public PayloadsCollector(ExecutionResultService executionResultService) {
        this.executionResultService = executionResultService;
    }

    public List<Integer> collect(List<Integer> wantedPayloadsIds) {
        if (wantedPayloadsIds.size() == 1 && wantedPayloadsIds.get(0) == -1) { //broadcast
            return executionResultService.getAllResultsIds().stream().toList();
        }

        return wantedPayloadsIds;
    }
}