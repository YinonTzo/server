package com.company.repositories;

import com.company.common.statuses.ExecutionStatus;
import com.company.entities.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class is a repository for storing and retrieving "ExecutionResult" entities in a database using JPA.
 * It provides methods to save, update and retrieve execution results,
 * and to retrieve a list of all the result IDs that are stored in the database.
 */

@AllArgsConstructor
@Slf4j
public class ExecutionResultRepository {

    private final EntityManagerFactory emf;

    public ExecutionResult saveAndSetMessageId(ExecutionResult executionResult) {
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(executionResult);
            em.flush();

            executionResult.setMessageId(executionResult.getId());
            executionResult.setStatus(ExecutionStatus.SENT);

            em.merge(executionResult);

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(e.getMessage());
        } finally {
            em.close();
        }

        return executionResult;
    }

    public Integer save(ExecutionResult executionResult) {
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(executionResult);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(e.getMessage());
        } finally {
            em.close();
        }

        return executionResult.getId();
    }

    public ExecutionResult update(ExecutionResult executionResult) {
        ExecutionResult updatedExecutionResult = null;
        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            updatedExecutionResult = em.merge(executionResult);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(e.getMessage());
        } finally {
            em.close();
        }

        return updatedExecutionResult;
    }

    public List<ExecutionResult> findAllByPayloadId(int payloadId) {
        List<ExecutionResult> executionResults = null;

        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            String hql = "SELECT result FROM ExecutionResult result where result.messageId = :payloadId";
            TypedQuery<ExecutionResult> tq = em.createQuery(hql, ExecutionResult.class);
            tq.setParameter("payloadId", payloadId);

            em.getTransaction().commit();

            executionResults = tq.getResultList();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            log.error(e.getMessage());
        } finally {
            em.close();
        }

        return executionResults;
    }

    public List<Integer> findAllIds() {
        List<Integer> allIds = null;

        var em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            String hql = "SELECT result.messageId FROM ExecutionResult result";
            TypedQuery<Integer> tq = em.createQuery(hql, Integer.class);

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
}