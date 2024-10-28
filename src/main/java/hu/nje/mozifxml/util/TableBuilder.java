package hu.nje.mozifxml.util;

import hu.nje.mozifxml.controller.model.MoviePerformance;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.TableColumn;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static hu.nje.mozifxml.util.Constant.COLUMN_CINEMA_NAME;
import static hu.nje.mozifxml.util.Constant.COLUMN_DATE;
import static hu.nje.mozifxml.util.Constant.COLUMN_ID;
import static hu.nje.mozifxml.util.Constant.COLUMN_INCOME;
import static hu.nje.mozifxml.util.Constant.COLUMN_MOVIE_TITLE;
import static hu.nje.mozifxml.util.Constant.COLUMN_VIEWER;
import static hu.nje.mozifxml.util.Constant.DATE_PATTERN;

public class TableBuilder {
    private static final String TEXT_ALIGN_CENTER = "-fx-alignment: CENTER;";
    private static final String TEXT_ALIGN_RIGHT = "-fx-alignment: CENTER-RIGHT;";

    public static List<TableColumn<MoviePerformance, ?>> createDbColumn() {
        TableColumn<MoviePerformance, Long> idCol = new TableColumn<>(COLUMN_ID);
        idCol.setPrefWidth(35);
        idCol.setStyle(TEXT_ALIGN_CENTER);
        idCol.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getPerformanceId()).asObject()
        );

        TableColumn<MoviePerformance, String> movieTitleCol = new TableColumn<>(COLUMN_MOVIE_TITLE);
        movieTitleCol.setPrefWidth(200);
        movieTitleCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getMovieTitle())
        );

        TableColumn<MoviePerformance, String> cinemaNameCol = new TableColumn<>(COLUMN_CINEMA_NAME);
        cinemaNameCol.setPrefWidth(200);
        cinemaNameCol.setStyle(TEXT_ALIGN_CENTER);
        cinemaNameCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getCinemaName() + "(" + cellData.getValue().getCinemaCity() + ")")
        );


        TableColumn<MoviePerformance, String> dateCol = new TableColumn<>(COLUMN_DATE);
        dateCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getPerformanceDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN))
                )
        );

        TableColumn<MoviePerformance, Long> viewerCol = new TableColumn<>(COLUMN_VIEWER);
        viewerCol.setPrefWidth(100);
        viewerCol.setStyle(TEXT_ALIGN_RIGHT);
        viewerCol.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getNumberOfVisitor()).asObject()
        );

        TableColumn<MoviePerformance, String> incomeCol = new TableColumn<>(COLUMN_INCOME);
        incomeCol.setPrefWidth(100);
        incomeCol.setStyle(TEXT_ALIGN_RIGHT);
        incomeCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getIncome()))
        );

        return Arrays.asList(idCol, movieTitleCol, cinemaNameCol, dateCol, viewerCol, incomeCol);
    }

}
