package hu.nje.mozifxml.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "film")
@Entity

public class Film  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @Column(name = "cim", nullable = false)
    private String cim;
    @Column(name = "ev", nullable = false)
    private int bemutatasiEve;
    @Column(name = "hossz", nullable = false)
    private int filmHossza;
}
