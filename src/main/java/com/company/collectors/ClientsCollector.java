package com.company.collectors;

import com.company.services.ClientManagerService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The "ClientsCollector" class is responsible for collecting a list of client IDs based on a list of requested IDs.
 * The class implements the Collector interface that defines a generic method collect that takes a list of IDs
 * and returns a list of generic type T.
 */
public class ClientsCollector implements Collector<Long> {

    private final ClientManagerService clientManagerService;

    public ClientsCollector(ClientManagerService clientManagerService) {
        this.clientManagerService = clientManagerService;
    }

    public List<Long> collect(List<Integer> wantedClientsIds) {
        if (wantedClientsIds.size() == 1 && wantedClientsIds.get(0) == -1) { //broadcast
            return clientManagerService.getAllClientsIds();
        }

        return wantedClientsIds.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}