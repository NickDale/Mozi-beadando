package hu.nje.mozifxml.controller;

import hu.nje.mozifxml.controller.model.MovieFilter;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.entities.Cinema;
import hu.nje.mozifxml.service.PerformanceService;
import hu.nje.mozifxml.util.TableBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final PerformanceService performanceService = new PerformanceService();

    @FXML
    private ScrollPane menu1ScrollPane, menu2ScrollPane;
    @FXML
    private TableView<MoviePerformance> performanceTable_menu1, performanceTable_menu2;
    @FXML
    private CheckBox caseInsensitiveCheckbox;
    @FXML
    private ComboBox<Cinema> cinemaCombobox;
    @FXML
    private RadioButton likeSearch;
    @FXML
    private TextField movieTitleSearchText;

    /**
     * Kilépés menüpont
     */
    @FXML
    protected void onClickExit() {
        System.exit(0);
    }

    /**
     * 1. feladat - Adatbázis menü - Olvas almenü
     */
    @FXML
    protected void dbReadMenuItem() {
        menu1ScrollPane.setVisible(true);
        menu2ScrollPane.setVisible(false);
        this.renderTableData(performanceTable_menu1);
        performanceTable_menu1.setItems(FXCollections.observableArrayList(performanceService.listPerformances()));
    }

    /**
     * 1. feladat - Asatbázis menü - Olvas2 almenü
     */
    @FXML
    protected void dbReadMenuItem2() {
        menu1ScrollPane.setVisible(false);
        menu2ScrollPane.setVisible(true);

        this.renderTableData(performanceTable_menu2);
        clearSearchPerformances();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void searchPerformances(ActionEvent actionEvent) {
        final MovieFilter movieFilter = new MovieFilter();
        SingleSelectionModel<Cinema> selectionModel = this.cinemaCombobox.getSelectionModel();
        if (!selectionModel.isEmpty()) {
            movieFilter.setCinemaId(selectionModel.getSelectedItem().getId());
        }

        movieFilter.setMovieName(this.movieTitleSearchText.getText());
        movieFilter.setCaseInsensitiveSearch(this.caseInsensitiveCheckbox.isSelected());
        movieFilter.setUseLikeForMovieTitleSearch(this.likeSearch.isSelected());
        Platform.runLater(() -> {
            performanceTable_menu2.setItems(FXCollections.observableArrayList(performanceService.listPerformancesByFilter(movieFilter)));
            performanceTable_menu2.refresh();
        });
    }

    private void renderTableData(TableView<MoviePerformance> tableView) {
        tableView.getColumns().clear();
        tableView.getColumns().addAll(TableBuilder.createDbColumn());
    }

    private void clearSearchPerformances() {
        this.movieTitleSearchText.clear();
        this.cinemaCombobox.getSelectionModel().clearSelection();
        this.caseInsensitiveCheckbox.setSelected(false);
        this.performanceTable_menu2.getItems().clear();
    }
}