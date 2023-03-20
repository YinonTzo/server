package com.company.plugins;

import com.company.commands.CLIToServerCommand;
import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.clientToServer.ExecutionData;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import com.company.common.messages.serverToCLI.CommandResults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The "DisplayCommandsResultCommand" class is a concrete subclass of the "CLIToServerCommand" class.
 * It is intended to retrieve data about executions from the server,
 * and returns the results as a "CommandResults" object.
 */
public class DisplayCommandsResultCommand extends CLIToServerCommand {

    public static final String COMMAND_NAME = "DisplayCommandResult";

    public DisplayCommandsResultCommand(ClientManagerService clientManagerService,
                                        ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);
    }

    @Override
    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        Map<Integer, List<ExecutionData>> payloadIdToResult = new HashMap<>();

        List<Integer> collectWantedPayloadsIds = collectWantedPayloads(cliRequest.getRequestIds());

        collectWantedPayloadsIds.forEach(id ->
                payloadIdToResult.put(id, executionResultService.getCommandResults(id)));

        CommandResults commandResults = new CommandResults();
        commandResults.setType(cliRequest.getType());
        commandResults.setPayloadIdToResult(payloadIdToResult);
        return commandResults;
    }

    private List<Integer> collectWantedPayloads(List<Integer> wantedPayloadsIds) {
        if (wantedPayloadsIds.contains(-1)) { //broadcast
            return executionResultService.getAllResultsIds().stream().toList();
        }

        return wantedPayloadsIds;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
