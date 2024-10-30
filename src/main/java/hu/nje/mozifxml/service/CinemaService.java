package hu.nje.mozifxml.service;

import hu.nje.mozifxml.entities.Cinema;

import java.util.List;

public class CinemaService extends AbstractService {

    public List<Cinema> listAllCinema() {
        return super.findAll(Cinema.FIND_ALL, Cinema.class);
    }

    public boolean saveCinema(final Cinema cinema) {
        return super.save(cinema);
    }
}
