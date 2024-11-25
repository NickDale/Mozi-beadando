package hu.nje.mozifxml.util;

import com.oanda.v20.trade.Trade;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

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


    public static List<TableColumn<Trade, ?>> positionTableRenderer() {
        TableColumn<Trade, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(20);

        TableColumn<Trade, String> instrumentColumn = new TableColumn<>("Instrument");
        instrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrument"));
        instrumentColumn.setPrefWidth(75);

        TableColumn<Trade, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(75);

        TableColumn<Trade, String> openTimeColumn = new TableColumn<>("Open\nTime");
        openTimeColumn.setCellValueFactory(new PropertyValueFactory<>("openTime"));
        openTimeColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        Helper.format.apply(cellData.getValue().getOpenTime())
                )
        );

        TableColumn<Trade, String> stateColumn = new TableColumn<>("State");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        TableColumn<Trade, Integer> initialUnitsColumn = new TableColumn<>("Initial\nUnits");
        initialUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("initialUnits"));
        initialUnitsColumn.setPrefWidth(50);

        TableColumn<Trade, Double> marginRequiredColumn = new TableColumn<>("Initial\nMargin\nRequired");
        marginRequiredColumn.setCellValueFactory(new PropertyValueFactory<>("initialMarginRequired"));
        marginRequiredColumn.setPrefWidth(50);

        TableColumn<Trade, Integer> currentUnitsColumn = new TableColumn<>("Current\nUnits");
        currentUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("currentUnits"));
        currentUnitsColumn.setPrefWidth(50);

        TableColumn<Trade, Double> realizedPLColumn = new TableColumn<>("Realized\nPL");
        realizedPLColumn.setCellValueFactory(new PropertyValueFactory<>("realizedPL"));

        TableColumn<Trade, Double> unrealizedPLColumn = new TableColumn<>("Unrealized\nPL");
        unrealizedPLColumn.setCellValueFactory(new PropertyValueFactory<>("unrealizedPL"));

        TableColumn<Trade, Double> marginUsedColumn = new TableColumn<>("Margin\nUsed");
        marginUsedColumn.setCellValueFactory(new PropertyValueFactory<>("marginUsed"));

        TableColumn<Trade, String> avgClosePriceColumn = new TableColumn<>("Average\nClose\nPrice");
        avgClosePriceColumn.setCellValueFactory(new PropertyValueFactory<>("averageClosePrice"));

        return Arrays.asList(idColumn, instrumentColumn, priceColumn, openTimeColumn, stateColumn, initialUnitsColumn,
                marginRequiredColumn, currentUnitsColumn, realizedPLColumn, unrealizedPLColumn, marginUsedColumn, avgClosePriceColumn);
    }


    public static List<TableColumn<MoviePerformance, ?>> createPerformanceDbColumn() {
        TableColumn<MoviePerformance, Long> idCol = new TableColumn<>(COLUMN_ID);
        idCol.setPrefWidth(50);
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
