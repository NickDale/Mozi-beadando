package hu.nje.mozifxml.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MovieService {


    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");

    public void createUser(String name, String email) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.getTransaction().commit();
        em.close();
    }


    public void close() {
        emf.close();
    }
}

