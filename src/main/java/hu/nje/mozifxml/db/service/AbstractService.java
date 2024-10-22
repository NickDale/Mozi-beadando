package hu.nje.mozifxml.db.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class AbstractService {
    protected final EntityManager em;

    AbstractService() {
        EntityManagerFactory myPersistenceUnit = Persistence.createEntityManagerFactory("myPersistenceUnit");
        this.em = myPersistenceUnit.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return em;
    }
}
