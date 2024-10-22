package hu.nje.mozifxml.db.service;

public class MovieService extends AbstractService {

    private static MovieService instance;

    private MovieService() {
    }

    public static MovieService instanceOf() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }


    public void listMovies() {
        //TODO: implement
    }

    public void listMoviesByFilter() {
        //TODO: implement
    }

    public void createMovie() {
        //TODO: implement
        em.getTransaction().begin();

        em.getTransaction().commit();
        em.close();
    }

    public void updateMovie() {
        //TODO: implement
    }

    public void deleteMovie() {
        //TODO: implement
    }

}

