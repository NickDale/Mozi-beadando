package hu.nje.mozifxml.db.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Table(name = "eloadas")
@Entity
public class Performance extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "filmid", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "moziid", referencedColumnName = "id")
    private Cinema cinema;

    @Column(name = "nezoszam", nullable = false)
    private int numberOfViewers;
    @Column(name = "bevetel", nullable = false)
    private double income;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Performance that)) return false;
        return Objects.equals(this.id, that.id)
                && numberOfViewers == that.numberOfViewers
                && Double.compare(income, that.income) == 0
                && Objects.equals(movie, that.movie)
                && Objects.equals(cinema, that.cinema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, cinema, numberOfViewers, income);
    }
}
