package hu.nje.mozifxml.controller;

import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.service.PerformanceService;
import hu.nje.mozifxml.util.TableBuilder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final PerformanceService performanceService = new PerformanceService();

    @FXML
    private ScrollPane menu1ScrollPane, menu2ScrollPane;
    @FXML
    private TableView<MoviePerformance> performanceTable_menu1, performanceTable_menu2;

    @FXML
    protected void onClickExit() {
        System.exit(0);
    }

    @FXML
    protected void dbReadMenuItem() {
        menu1ScrollPane.setVisible(true);
        menu2ScrollPane.setVisible(false);
        this.renderTableData();
    }

    @FXML
    protected void dbReadMenuItem2() {
        menu1ScrollPane.setVisible(false);
        menu2ScrollPane.setVisible(true);
        performanceService.listPerformancesByFilter("dsd", 1L, true);

        performanceTable_menu2.getColumns().clear();
        performanceTable_menu2.getColumns().addAll(TableBuilder.createDbColumn());

        performanceTable_menu2.setItems(FXCollections.observableArrayList(performanceService.listPerformances()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderTableData();
    }

    private void renderTableData() {

        performanceTable_menu1.getColumns().clear();
        performanceTable_menu1.getColumns().addAll(TableBuilder.createDbColumn());

        performanceTable_menu1.setItems(FXCollections.observableArrayList(performanceService.listPerformances()));
    }

}