package hu.nje.mozifxml.controller.model;

import hu.nje.mozifxml.entities.Performance;

import java.time.LocalDate;


public class MoviePerformance {

    private long performanceId;
    private String movieTitle;
    private String cinemaName;
    private String cinemaCity;
    private LocalDate performanceDate;
    private int numberOfVisitor;
    private Double income;

    public MoviePerformance() {

    }

    public MoviePerformance(long performanceId, String movieTitle,
                            String cinemaName, String cinemaCity,
                            LocalDate performanceDate, int numberOfVisitor, Double income) {
        this.performanceId = performanceId;
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.cinemaCity = cinemaCity;
        this.performanceDate = performanceDate;
        this.numberOfVisitor = numberOfVisitor;
        this.income = income;
    }

    public long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(long performanceId) {
        this.performanceId = performanceId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaCity() {
        return cinemaCity;
    }

    public void setCinemaCity(String cinemaCity) {
        this.cinemaCity = cinemaCity;
    }

    public LocalDate getPerformanceDate() {
        return performanceDate;
    }

    public void setPerformanceDate(LocalDate performanceDate) {
        this.performanceDate = performanceDate;
    }

    public int getNumberOfVisitor() {
        return numberOfVisitor;
    }

    public void setNumberOfVisitor(int numberOfVisitor) {
        this.numberOfVisitor = numberOfVisitor;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }
}
