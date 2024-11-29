package hu.nje.mozifxml.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "film")
@Entity
public class Movie extends AbstractEntity {

    @Column(name = "cim", nullable = false)
    private String title;
    @Column(name = "ev", nullable = false)
    private int releaseYear;
    @Column(name = "hossz", nullable = false)
    private int runTimeInMin;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRunTimeInMin() {
        return runTimeInMin;
    }

    public void setRunTimeInMin(int runTimeInMin) {
        this.runTimeInMin = runTimeInMin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return releaseYear == movie.releaseYear
                && Objects.equals(this.id, movie.id)
                && runTimeInMin == movie.runTimeInMin
                && Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, releaseYear, runTimeInMin);
    }
}
