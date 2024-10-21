package hu.nje.mozifxml.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MovieService {

    private static MovieService instance;
    private final EntityManager em;
    private MovieService() {
        EntityManagerFactory myPersistenceUnit = Persistence.createEntityManagerFactory("myPersistenceUnit");
        this.em = myPersistenceUnit.createEntityManager();
    }

    public static MovieService instanceOf() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }


    public void listMovies() {
        //TODO: implement
    }

    public void listMoviesByFilter() {
        //TODO: implement
    }

    public void createMovie() {
        //TODO: implement
        em.getTransaction().begin();

        em.getTransaction().commit();
        em.close();
    }

    public void updateMovie() {
        //TODO: implement
    }

    public void deleteMovie() {
        //TODO: implement
    }

}

