package hu.nje.mozifxml.entities;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static hu.nje.mozifxml.util.Constant.DATE_PATTERN;

@Table(name = "eloadas")
@Entity

@NamedQuery(name = Performance.FIND_ALL, query = "SELECT p From Performance p")
public class Performance extends AbstractEntity {
    public static final String FIND_ALL = "Performance.findAll";

    @ManyToOne(optional = false)
    @JoinColumn(name = "filmid", referencedColumnName = "id", updatable = false, insertable = false)
    private Movie movie;

    @Column(name = "filmid")
    private long movieId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "moziid", referencedColumnName = "id", updatable = false, insertable = false)
    private Cinema cinema;

    @Column(name = "moziid")
    private long cinemaId;

    @Column(name = "datum", nullable = false)
    private LocalDate date;

    @Column(name = "nezoszam", nullable = false)
    private int numberOfViewers;
    @Column(name = "bevetel", nullable = false)
    private double income;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(long cinemaId) {
        this.cinemaId = cinemaId;
    }

    public int getNumberOfViewers() {
        return numberOfViewers;
    }

    public void setNumberOfViewers(int numberOfViewers) {
        this.numberOfViewers = numberOfViewers;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Performance that)) return false;
        return Objects.equals(this.id, that.id)
                && numberOfViewers == that.numberOfViewers
                && Double.compare(income, that.income) == 0
                && Objects.equals(movieId, that.movieId)
                && Objects.equals(cinemaId, that.cinemaId)
                && Objects.equals(date, that.date)
                && Objects.equals(movie, that.movie)
                && Objects.equals(cinema, that.cinema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieId, movie, date, cinemaId, cinema, numberOfViewers, income);
    }

    @Override
    public String toString() {
        return this.movie.getTitle()
                + " - "
                + this.getCinema().getName()
                + " - "
                + this.date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
}
