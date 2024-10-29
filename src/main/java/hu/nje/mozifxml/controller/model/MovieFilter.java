package hu.nje.mozifxml.controller.model;

public class MovieFilter {
    private String movieName;
    private boolean useLikeForMovieTitleSearch;
    private boolean caseInsensitiveSearch;
    private Long cinemaId;

    public MovieFilter() {

    }

    public MovieFilter(String movieName, boolean useLikeForMovieTitleSearch, boolean caseInsensitiveSearch, Long cinemaId) {
        this.movieName = movieName;
        this.useLikeForMovieTitleSearch = useLikeForMovieTitleSearch;
        this.caseInsensitiveSearch = caseInsensitiveSearch;
        this.cinemaId = cinemaId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public boolean isUseLikeForMovieTitleSearch() {
        return useLikeForMovieTitleSearch;
    }

    public void setUseLikeForMovieTitleSearch(boolean useLikeForMovieTitleSearch) {
        this.useLikeForMovieTitleSearch = useLikeForMovieTitleSearch;
    }

    public boolean isCaseInsensitiveSearch() {
        return caseInsensitiveSearch;
    }

    public void setCaseInsensitiveSearch(boolean caseInsensitiveSearch) {
        this.caseInsensitiveSearch = caseInsensitiveSearch;
    }

    public Long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Long cinemaId) {
        this.cinemaId = cinemaId;
    }
}
