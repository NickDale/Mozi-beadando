package hu.nje.mozifxml.service;

import hu.nje.mozifxml.controller.model.MovieFilter;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.entities.Performance;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static hu.nje.mozifxml.util.Constant.LIKE_PER_CENT;

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

    public List<MoviePerformance> listMoviePerformances() {
        return this.listPerformances().stream().map(moviePerformanceMapper).toList();
    }

    public List<Performance> listPerformances() {
        return super.findAll(Performance.FIND_ALL, Performance.class);
    }

    public List<MoviePerformance> listPerformancesByFilter(MovieFilter movieFilter) {
        return this.listPerformancesByFilter2(movieFilter)
                .stream()
                .map(moviePerformanceMapper)
                .toList();
    }

    public void deletePerformance(Performance performance) {
        super.delete(performance);
    }

    private List<Performance> listPerformancesByFilter2(MovieFilter movieFilter) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
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
                        cb.equal(root.get("cinema").get("id"), movieFilter.getCinemaId())
                );
            }

            cq.where(predicates.toArray(Predicate[]::new));
            cq.orderBy(
                    cb.asc(
                            root.get("movie").get("title")
                    ),
                    cb.desc(root.get("date"))
            );

            return entityManager.createQuery(cq).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (entityManager !=null){
                entityManager.close();
            }
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
                                root.get("movie").get("title")
                        ),
                        LIKE_PER_CENT + movieFilter.getMovieName().toLowerCase() + LIKE_PER_CENT
                );
            }
            return cb.like(
                    root.get("movie").get("title"),
                    LIKE_PER_CENT + movieFilter.getMovieName() + LIKE_PER_CENT
            );
        }
        if (movieFilter.isCaseInsensitiveSearch()) {
            return cb.equal(
                    cb.lower(
                            root.get("movie").get("title")
                    ),
                    movieFilter.getMovieName().toLowerCase()
            );
        }
        return cb.equal(
                root.get("movie").get("title"),
                movieFilter.getMovieName()
        );
    }
}
