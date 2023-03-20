package com.company.services.map;

import com.company.common.messages.clientToServer.ExecutionData;
import com.company.server.Server;
import com.company.common.statuses.ExecutionStatus;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;

import java.util.*;

public class ExecutionResult implements ExecutionResultService {

    private final ClientManagerService clientManagerService;

    private final Map<Integer, List<ExecutionData>> payloadIdToResult = new HashMap<>();
    private final Map<Server.ClientHandler, List<Integer>> ClientHandlerToPayloadIds = new HashMap<>();
    private Integer messageId = 0;

    public ExecutionResult(ClientManagerService clientManagerService) {
        this.clientManagerService = clientManagerService;
    }

    @Override
    public ExecutionData save(Server.ClientHandler clientHandler) {
        ExecutionData newExecutionData = new ExecutionData(messageId++, ExecutionStatus.SENT);
        payloadIdToResult.putIfAbsent(newExecutionData.getMessageId(), new ArrayList<>());
        addResult(newExecutionData);

        ClientHandlerToPayloadIds.putIfAbsent(clientHandler, new ArrayList<>());
        ClientHandlerToPayloadIds.get(clientHandler).add(newExecutionData.getMessageId());
        return newExecutionData;
    }

    @Override
    public void addResult(ExecutionData result) {
        payloadIdToResult.get(result.getMessageId()).add(result);
    }

    @Override
    public List<ExecutionData> getCommandResults(int commandResultId) {
        return payloadIdToResult.get(commandResultId);
    }

    @Override
    public Set<Integer> getAllResultsIds() {
        return payloadIdToResult.keySet();
    }

    @Override
    public Optional<List<Integer>> getPayloadsIdsByClientId(int clientId) {
        return Optional.ofNullable(clientManagerService.getClient(clientId))
                .map(ClientHandlerToPayloadIds::get);
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "payloadIdToResult=" + payloadIdToResult +
                ", ClientHandlerToPayloadIds=" + ClientHandlerToPayloadIds +
                '}';
    }
}
