package hu.nje.mozifxml.service;

import hu.nje.mozifxml.controller.model.MovieFilter;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.entities.Cinema_;
import hu.nje.mozifxml.entities.Movie_;
import hu.nje.mozifxml.entities.Performance;
import hu.nje.mozifxml.entities.Performance_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PerformanceService extends AbstractService {

    private final Function<Performance, MoviePerformance> moviePerformanceMapper = p -> {
        MoviePerformance moviePerformance = new MoviePerformance();
        moviePerformance.setPerformanceId(p.getId());
        moviePerformance.setMovieTitle(p.getMovie().getTitle());
        moviePerformance.setCinemaName(p.getCinema().getName());
        moviePerformance.setCinemaCity(p.getCinema().getCity());
        moviePerformance.setPerformanceDate(p.getDate());
        moviePerformance.setNumberOfVisitor(p.getNumberOfViewers());
        moviePerformance.setIncome(p.getIncome());
        return moviePerformance;
    };

    public List<MoviePerformance> listPerformances() {
        try (EntityManager entityManager = this.entityManagerFactory.createEntityManager()) {
            final List<Performance> resultList = entityManager.createNamedQuery(Performance.FIND_ALL, Performance.class)
                    .getResultList();

            return resultList.stream().map(moviePerformanceMapper).toList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<MoviePerformance> listPerformancesByFilter(MovieFilter movieFilter) {
        return this.listPerformancesByFilter2(movieFilter)
                .stream()
                .map(moviePerformanceMapper)
                .toList();
    }

    private List<Performance> listPerformancesByFilter2(MovieFilter movieFilter) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            final CriteriaQuery<Performance> cq = cb.createQuery(Performance.class);
            final Root<Performance> root = cq.from(Performance.class);
            final List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(movieFilter.getMovieName()) && !movieFilter.getMovieName().trim().isEmpty()) {
                predicates.add(
                        this.createMovieTitleSearchPredicate(cb, root, movieFilter)
                );
            }

            if (Objects.nonNull(movieFilter.getCinemaId())) {
                predicates.add(
                        cb.equal(root.get(Performance_.CINEMA).get(Cinema_.ID), movieFilter.getCinemaId())
                );
            }

            cq.where(predicates.toArray(Predicate[]::new));
            cq.orderBy(
                    cb.asc(
                            root.get(Performance_.MOVIE).get(Movie_.TITLE)
                    ),
                    cb.desc(root.get(Performance_.DATE))
            );

            return entityManager.createQuery(cq).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    private Predicate createMovieTitleSearchPredicate(final CriteriaBuilder cb,
                                                      final Root<Performance> root,
                                                      MovieFilter movieFilter) {
        if (movieFilter.isUseLikeForMovieTitleSearch()) {
            if (movieFilter.isCaseInsensitiveSearch()) {
                return cb.like(
                        cb.lower(
                                root.get(Performance_.MOVIE).get(Movie_.TITLE)
                        ),
                        LIKE_PER_CENT + movieFilter.getMovieName().toLowerCase() + LIKE_PER_CENT
                );
            }
            return cb.like(
                    root.get(Performance_.MOVIE).get(Movie_.TITLE),
                    LIKE_PER_CENT + movieFilter.getMovieName() + LIKE_PER_CENT
            );
        }
        if (movieFilter.isCaseInsensitiveSearch()) {
            return cb.equal(
                    cb.lower(
                            root.get(Performance_.MOVIE).get(Movie_.TITLE)
                    ),
                    movieFilter.getMovieName().toLowerCase()
            );
        }
        return cb.equal(
                root.get(Performance_.MOVIE).get(Movie_.TITLE),
                movieFilter.getMovieName()
        );
    }
}
