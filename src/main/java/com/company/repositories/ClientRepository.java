package com.company.repositories;

import com.company.entities.Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class is a repository for storing and retrieving "Client" entities in a database using JPA.
 */

@AllArgsConstructor
@Slf4j
public class ClientRepository {

    private final EntityManagerFactory emf;

    public Client save(Client client) {
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(Arrays.toString(e.getStackTrace()));
        } finally {
            em.close();
        }

        return client;
    }

    public Optional<Client> findById(Long clientId) {
        Optional<Client> optionalClient = null;
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            String hql = "SELECT c FROM Client c LEFT JOIN FETCH c.results WHERE c.id = :clientId";
            TypedQuery<Client> tq = em.createQuery(hql, Client.class);
            tq.setParameter("clientId", clientId);

            em.getTransaction().commit();

            optionalClient = Optional.of(tq.getSingleResult());
        } catch (NoResultException e) {
            optionalClient = Optional.empty();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(Arrays.toString(e.getStackTrace()));
        } finally {
            em.close();
        }

        return optionalClient;
    }

    public List<Long> findAllIds() {
        List<Long> allIds = null;

        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            String hql = "SELECT c.id FROM Client c";
            TypedQuery<Long> tq = em.createQuery(hql, Long.class);

            em.getTransaction().commit();

            allIds = tq.getResultList();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(e.getMessage());
        } finally {
            em.close();
        }

        return allIds;
    }

    public void update(Client client) {
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(client);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(Arrays.toString(e.getStackTrace()));
        } finally {
            em.close();
        }
    }
}