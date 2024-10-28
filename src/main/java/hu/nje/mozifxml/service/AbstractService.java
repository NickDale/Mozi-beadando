package hu.nje.mozifxml.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

abstract class AbstractService {

    public static final String LIKE_PER_CENT = "%";
    protected final EntityManagerFactory entityManagerFactory;

    AbstractService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }
}
