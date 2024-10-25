package hu.nje.mozifxml.db.service;

import hu.nje.mozifxml.db.entities.Performance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PerformanceService extends AbstractService {

    public List<Performance> listPerformancesByFilter(final String cinemaName,
                                                      final Long filmId,
                                                      final boolean useLikeForCinemaName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            final CriteriaQuery<Performance> cq = cb.createQuery(Performance.class);
            final Root<Performance> performance = cq.from(Performance.class);
            final List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(cinemaName) && !cinemaName.trim().isEmpty()) {
                if (useLikeForCinemaName) {
                    predicates.add(
                            cb.like(
                                    cb.lower(performance.get("cinema").get("name")),
                                    LIKE_PER_CENT + cinemaName.toLowerCase() + LIKE_PER_CENT)
                    );
                } else {
                    predicates.add(
                            cb.equal(
                                    cb.lower(performance.get("cinema").get("name")), cinemaName.toLowerCase()
                            )
                    );
                }
            }

            if (Objects.nonNull(filmId)) {
                predicates.add(
                        cb.equal(performance.get("movie").get("id"), filmId)
                );
            }
            //TODO: add one more field

            cq.where(predicates.toArray(Predicate[]::new));

            TypedQuery<Performance> query = entityManager.createQuery(cq);
            return query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<?> listPerformances() {
        try (EntityManager entityManager = this.entityManagerFactory.createEntityManager()) {
            return entityManager.createNamedQuery(Performance.FIND_ALL, Performance.class)
                    .setMaxResults(500)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }
}
