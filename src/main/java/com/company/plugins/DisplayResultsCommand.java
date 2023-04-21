package com.company.plugins;

import com.company.commands.CLIToServerCommand;
import com.company.collectors.PayloadsCollector;
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
 * The "DisplayResultsCommand" class is a concrete subclass of the "CLIToServerCommand" class.
 * It is intended to retrieve data about executions from the server,
 * and returns the results as a "CommandResults" object.
 */
public class DisplayResultsCommand extends CLIToServerCommand {

    public static final String COMMAND_NAME = "DisplayResults";

    private PayloadsCollector payloadsCollector;

    public DisplayResultsCommand(ClientManagerService clientManagerService,
                                 ExecutionResultService executionResultService) {

        super(clientManagerService, executionResultService);

        payloadsCollector = new PayloadsCollector(executionResultService);
    }

    @Override
    public BaseServerToCLI execute(BaseCLIToServer cliRequest) {
        Map<Integer, List<ExecutionData>> payloadIdToResult = new HashMap<>();

        List<Integer> wantedPayloadsIds = payloadsCollector.collect(cliRequest.getRequestIds());

        wantedPayloadsIds.forEach(id ->
                payloadIdToResult.put(id, executionResultService.getResultsById(id).orElse(null)));

        CommandResults commandResults = new CommandResults();
        commandResults.setType(cliRequest.getType());
        commandResults.setPayloadIdToResult(payloadIdToResult);
        return commandResults;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    public void setPayloadsCollector(PayloadsCollector payloadsCollector) {
        this.payloadsCollector = payloadsCollector;
    }
}