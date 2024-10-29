package hu.nje.mozifxml.controller;

import hu.nje.mozifxml.controller.model.MovieFilter;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.entities.Cinema;
import hu.nje.mozifxml.entities.Performance;
import hu.nje.mozifxml.service.CinemaService;
import hu.nje.mozifxml.service.PerformanceService;
import hu.nje.mozifxml.util.TableBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import static java.util.Optional.ofNullable;

public class MainController implements Initializable {
    private final PerformanceService performanceService = new PerformanceService();
    private final CinemaService cinemaService = new CinemaService();

    @FXML
    private ScrollPane menu1ScrollPane, menu2ScrollPane;

    @FXML
    private Pane deletePerformancePane, editMoviePanel, createMoviePanel;
    @FXML
    private TableView<MoviePerformance> performanceTable_menu1, performanceTable_menu2;
    @FXML
    private CheckBox caseInsensitiveCheckbox;
    @FXML
    private ComboBox<Cinema> cinemaCombobox, cinemaComboBox;
    @FXML
    private ComboBox<Performance> performanceCombobox;
    @FXML
    private RadioButton likeSearch;
    @FXML
    private TextField movieTitleSearchText, editMovieCapacity, editMovieCity, editMovieName;

    @FXML
    private Button deletePerformanceBtn;

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
        deletePerformancePane.setVisible(false);
        editMoviePanel.setVisible(false);

        this.renderTableData(performanceTable_menu1);
        performanceTable_menu1.setItems(FXCollections.observableArrayList(performanceService.listMoviePerformances()));
    }

    /**
     * 1. feladat - Asatbázis menü - Olvas2 almenü
     */
    @FXML
    protected void dbReadMenuItem2() {
        menu1ScrollPane.setVisible(false);
        menu2ScrollPane.setVisible(true);
        deletePerformancePane.setVisible(false);
        editMoviePanel.setVisible(false);

        this.renderTableData(performanceTable_menu2);
        cinemaCombobox.setItems(FXCollections.observableArrayList(cinemaService.listAllCinema()));
        clearSearchPerformances();
    }

    @FXML
    public void dbCreateMenuItem() {
    }

    @FXML
    public void dbEditMenuItem() {
        menu1ScrollPane.setVisible(false);
        menu2ScrollPane.setVisible(false);
        deletePerformancePane.setVisible(false);
        editMoviePanel.setVisible(true);

        cinemaComboBox.setItems(FXCollections.observableArrayList(cinemaService.listAllCinema()));
    }

    @FXML
    public void dbDeleteMenuItem() {
        menu1ScrollPane.setVisible(false);
        menu2ScrollPane.setVisible(false);
        deletePerformancePane.setVisible(true);
        editMoviePanel.setVisible(false);

        performanceCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deletePerformanceBtn.setDisable(newValue == null);
        });
        this.setPerformanceCombobox();
    }

    public void deletePerformance() {
        Performance selectedItem = performanceCombobox.getSelectionModel().getSelectedItem();
        performanceService.deletePerformance(selectedItem);
        this.setPerformanceCombobox();
    }

    private void setPerformanceCombobox() {
        performanceCombobox.setItems(
                FXCollections.observableArrayList(performanceService.listPerformances())
        );
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


    public void cinemaSelected(ActionEvent actionEvent) {
        Cinema selectedItem = this.cinemaComboBox.getSelectionModel().getSelectedItem();

        this.editMovieCity.setText(
                ofNullable(selectedItem).map(Cinema::getCity).orElse(null)
        );
        this.editMovieName.setText(
                ofNullable(selectedItem).map(Cinema::getName).orElse(null)
        );
        this.editMovieCapacity.setText(
                ofNullable(selectedItem).map(c -> String.valueOf(c.getMaxCapacity())).orElse(null)
        );
    }
}