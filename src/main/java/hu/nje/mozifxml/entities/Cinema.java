package hu.nje.mozifxml.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.NamedQuery;

import java.util.Objects;

@Table(name = "mozi")
@Entity

@NamedQuery(name = Cinema.FIND_ALL, query = "SELECT c From Cinema c ORDER BY c.name ASC,c.city DESC ")
public class Cinema extends AbstractEntity {
    public static final String FIND_ALL = "Cinema.findAll";


    @Column(name = "nev", nullable = false)
    private String name;
    @Column(name = "varos", nullable = false)
    private String city;
    @Column(name = "ferohely", nullable = false)
    private int maxCapacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cinema cinema = (Cinema) o;
        return Objects.equals(this.id, cinema.id) &&
                maxCapacity == cinema.maxCapacity
                && Objects.equals(name, cinema.name)
                && Objects.equals(city, cinema.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, maxCapacity);
    }

    @Override
    public String toString() {
        return name + " (" + city + ")";
    }
}
