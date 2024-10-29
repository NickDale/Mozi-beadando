package hu.nje.mozifxml.service;

import hu.nje.mozifxml.entities.Cinema;
import jakarta.persistence.EntityManager;

import java.util.Collections;
import java.util.List;

public class CinemaService extends AbstractService {

    public List<Cinema> listAllCinema() {
        try (EntityManager entityManager = this.entityManagerFactory.createEntityManager()) {
            return entityManager.createNamedQuery(Cinema.FIND_ALL, Cinema.class)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }
}
