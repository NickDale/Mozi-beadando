package hu.nje.mozifxml.service;

import hu.nje.mozifxml.entities.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.List;

abstract class AbstractService {


    protected final EntityManagerFactory entityManagerFactory;

    AbstractService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    protected <T extends AbstractEntity> List<T> findAll(final String query, final Class<T> resultClass) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        try {
            return entityManager.createNamedQuery(query, resultClass)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(entityManager != null) {
                entityManager.clear();
            }
        }
        return Collections.emptyList();
    }

    protected <T extends AbstractEntity> void delete(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (!entityManager.contains(entity)) {
                entity = entityManager.merge(entity);
            }
            entityManager.remove(entity);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.clear();
        }
    }

    protected <T extends AbstractEntity> boolean save(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (entity.getId() == null) {
                entityManager.persist(entity);
            } else {
                entityManager.merge(entity);
            }
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            entityManager.clear();
        }
    }
}
