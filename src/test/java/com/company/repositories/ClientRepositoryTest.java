package com.company.repositories;

import com.company.common.statuses.ExecutionStatus;
import com.company.entities.Client;
import com.company.entities.ExecutionResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {

    private EntityManagerFactory emf;

    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        clientRepository = new ClientRepository(emf);
    }

    @AfterEach
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void save() {
        // given
        Client client = new Client();

        // when
        Client saved = clientRepository.save(client);

        // then
        Client foundClient = emf.createEntityManager().find(Client.class, saved.getId());

        assertNotNull(saved);
        assertNotNull(foundClient);
        assertEquals(saved.getId(), foundClient.getId());
    }

    @Test
    void findByIdWithExistenceClient() {
        // given
        Client client = new Client();

        Client saved = clientRepository.save(client);

        // when
        Optional<Client> optionalClient = clientRepository.findById(saved.getId());

        // then
        assertEquals(1L, saved.getId());
        assertTrue(optionalClient.isPresent());
        assertEquals(saved.getId(), optionalClient.get().getId());
    }

    @Test
    void findByIdWithMissedClient() {
        // no given

        // when
        Optional<Client> optionalClient = clientRepository.findById(123L);

        // then
        assertFalse(optionalClient.isPresent());
    }

    @Test
    void findAllIds() {
        // given
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();

        Client saved = clientRepository.save(client1);
        Client saved2 = clientRepository.save(client2);
        Client saved3 = clientRepository.save(client3);

        // when
        List<Long> allIds = clientRepository.findAllIds();

        // then
        assertEquals(3, allIds.size());
        assertTrue(allIds.contains(client1.getId()));
        assertTrue(allIds.contains(client2.getId()));
        assertTrue(allIds.contains(client3.getId()));
    }

    @Test
    void findAllIdsWithEmptyList() {
        //no given

        //when
        List<Long> allIds = clientRepository.findAllIds();

        //then
        assertEquals(0, allIds.size());
    }

    @Test
    void update() {
        // given
        Client saved = clientRepository.save(new Client());

        ExecutionResult executionResult1 = new ExecutionResult();
        executionResult1.setId(1);
        executionResult1.setMessageId(1);
        executionResult1.setStatus(ExecutionStatus.SENT);
        saved.getResults().add(executionResult1);

        ExecutionResult executionResult2 = new ExecutionResult();
        executionResult2.setId(2);
        executionResult2.setMessageId(1);
        executionResult2.setStatus(ExecutionStatus.RECEIVED);
        saved.getResults().add(executionResult2);


        // when
        clientRepository.update(saved);

        // then
        Client updatedClient = emf.createEntityManager().find(Client.class, saved.getId());

        assertEquals(2, updatedClient.getResults().size());
        assertEquals(1, updatedClient.getResults().get(0).getMessageId());
        assertEquals(1, updatedClient.getResults().get(1).getMessageId());
    }
}