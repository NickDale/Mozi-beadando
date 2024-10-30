package hu.nje.mozifxml.controller;

import hu.nje.mozifxml.controller.model.MovieFilter;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.entities.Cinema;
import hu.nje.mozifxml.entities.Performance;
import hu.nje.mozifxml.service.CinemaService;
import hu.nje.mozifxml.service.PerformanceService;
import hu.nje.mozifxml.util.TableBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static hu.nje.mozifxml.util.Constant.NUMBER_REGEX;
import static hu.nje.mozifxml.util.Constant.isNotEmpty;
import static hu.nje.mozifxml.util.Helper.onlyNumbersEventListener;
import static java.util.Objects.isNull;
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
    private Button deletePerformanceBtn, editMovieBtn;

    private Cinema selectedCinema;
    private final ChangeListener<String> stringChangeListener = (observable, oldValue, newValue) -> validateForm();

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
        this.changeView(menu1ScrollPane);

        this.renderTableData(performanceTable_menu1);
        performanceTable_menu1.setItems(FXCollections.observableArrayList(performanceService.listMoviePerformances()));
    }

    private <T extends Region> void changeView(final T selectedPane) {
        this.hideAllPane();
        selectedPane.setVisible(true);
    }

    private void hideAllPane() {
        List.of(deletePerformancePane, editMoviePanel)
                .forEach(p -> p.setVisible(false));
        List.of(menu1ScrollPane, menu2ScrollPane).forEach(p -> p.setVisible(false));
    }

    /**
     * 1. feladat - Asatbázis menü - Olvas2 almenü
     */
    @FXML
    protected void dbReadMenuItem2() {
        this.changeView(menu2ScrollPane);

        this.renderTableData(performanceTable_menu2);
        cinemaCombobox.setItems(FXCollections.observableArrayList(cinemaService.listAllCinema()));
        clearSearchPerformances();
    }

    /**
     * 1. feladat - Asatbázis menü - Ír almenü
     */
    @FXML
    public void dbCreateMenuItem() {
        this.changeView(createMoviePanel);
    }

    /**
     * 1. feladat - Asatbázis menü - Módosít almenü
     */

    @FXML
    public void dbEditMenuItem() {
        this.changeView(editMoviePanel);


        this.editMovieCity.textProperty().addListener(stringChangeListener);
        this.editMovieName.textProperty().addListener(stringChangeListener);
        this.editMovieCapacity.textProperty().addListener(stringChangeListener);

        onlyNumbersEventListener(editMovieCapacity);
        this.refreshCinemaCombo(cinemaComboBox);
    }

    private void refreshCinemaCombo(ComboBox<Cinema> comboBox) {
        comboBox.setItems(
                FXCollections.observableArrayList(cinemaService.listAllCinema())
        );
    }

    /**
     * 1. feladat - Asatbázis menü - Töröl almenü
     */
    @FXML
    public void dbDeleteMenuItem() {
        this.changeView(deletePerformancePane);

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
        this.hideAllPane();
    }

    public void searchPerformances(ActionEvent actionEvent) {
        final MovieFilter movieFilter = new MovieFilter();
        final SingleSelectionModel<Cinema> selectionModel = this.cinemaCombobox.getSelectionModel();
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
        selectedCinema = this.cinemaComboBox.getSelectionModel().getSelectedItem();

        final String city = ofNullable(selectedCinema).map(Cinema::getCity).orElse(null);
        final String cinemaName = ofNullable(selectedCinema).map(Cinema::getName).orElse(null);
        final String maxCapacity = ofNullable(selectedCinema).map(c -> String.valueOf(c.getMaxCapacity())).orElse(null);

        this.editMovieCity.setText(city);
        this.editMovieName.setText(cinemaName);
        this.editMovieCapacity.setText(maxCapacity);

        boolean isDisabled = isNotEmpty(cinemaName) && isNotEmpty(maxCapacity) && isNotEmpty(city);
        editMovieBtn.setDisable(!isDisabled);
    }

    private void validateForm() {
        if (isNull(selectedCinema)) {
            editMovieBtn.setDisable(true);
            return;
        }
        boolean isModified = !editMovieName.getText().equals(selectedCinema.getName())
                || !editMovieCity.getText().equals(selectedCinema.getCity())
                || !editMovieCapacity.getText().equals(String.valueOf(selectedCinema.getMaxCapacity()));

        boolean isNameValid = isNotEmpty(editMovieName.getText());
        boolean isCityValid = isNotEmpty(editMovieCity.getText());
        boolean isCapacityValid = editMovieCapacity.getText().matches(NUMBER_REGEX);

        editMovieBtn.setDisable(!(isModified && isNameValid && isCityValid && isCapacityValid));
    }

    public void editCinema(ActionEvent actionEvent) {
        if (isNull(selectedCinema)) {
            return;
        }

        selectedCinema.setCity(this.editMovieCity.getText());
        selectedCinema.setName(this.editMovieName.getText());
        selectedCinema.setMaxCapacity(Integer.parseInt(this.editMovieCapacity.getText()));
        cinemaService.saveCinema(selectedCinema);
        this.cleanCleanEditPage();
    }

    private void cleanCleanEditPage() {
        List.of(this.editMovieCity,
                this.editMovieName,
                this.editMovieCapacity
        ).forEach(f -> f.setText(null));
        this.refreshCinemaCombo(cinemaComboBox);
    }
}