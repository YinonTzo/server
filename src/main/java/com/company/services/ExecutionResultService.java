package com.company.services;

import com.company.common.messages.clientToServer.ExecutionData;
import com.company.server.Server;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExecutionResultService {

    ExecutionData save(Server.ClientHandler clientHandler);

    void addResult(ExecutionData result);

    List<ExecutionData> getCommandResults(int commandResultId);

    Set<Integer> getAllResultsIds();

    Optional<List<Integer>> getPayloadsIdsByClientId(int clientId);
}
