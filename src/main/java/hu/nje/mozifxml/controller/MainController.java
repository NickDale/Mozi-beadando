package hu.nje.mozifxml.controller;

import hu.nje.mozifxml.controller.model.MovieFilter;
import hu.nje.mozifxml.controller.model.MoviePerformance;
import hu.nje.mozifxml.entities.Cinema;
import hu.nje.mozifxml.entities.Performance;
import hu.nje.mozifxml.service.CinemaService;
import hu.nje.mozifxml.service.MNBService;
import hu.nje.mozifxml.service.PerformanceService;
import hu.nje.mozifxml.service.oanda.Item;
import hu.nje.mozifxml.service.oanda.OandaService;
import hu.nje.mozifxml.util.TableBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hu.nje.mozifxml.util.Constant.NUMBER_REGEX;
import static hu.nje.mozifxml.util.Constant.SUCCESSFULLY_SAVED;
import static hu.nje.mozifxml.util.Constant.SUCCESSFULLY_SAVED_MSG;
import static hu.nje.mozifxml.util.Constant.isNotEmpty;
import static hu.nje.mozifxml.util.Helper.alert;
import static hu.nje.mozifxml.util.Helper.onlyNumbersEventListener;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

public class MainController implements Initializable {
    private final PerformanceService performanceService = new PerformanceService();
    private final CinemaService cinemaService = new CinemaService();
    private final MNBService mnbService = new MNBService();
    private final OandaService oandaService = new OandaService();

    private final ChangeListener<String> newCinemaRegListener = (observable, oldValue, newValue) -> newCinemaRegValidator();
    private final ChangeListener<String> stringChangeListener = (observable, oldValue, newValue) -> validateForm();

    private ScheduledExecutorService executor;
    @FXML
    private ScrollPane menu1ScrollPane, menu2ScrollPane;
    @FXML
    private Pane deletePerformancePane, editMoviePanel, createMoviePanel, accountInfoPanel, parallelPanel, mnbFilterPane;
    @FXML
    private TableView<MoviePerformance> performanceTable_menu1, performanceTable_menu2;
    @FXML
    private TableView<Item> accountTableView;
    @FXML
    private CheckBox caseInsensitiveCheckbox;
    @FXML
    private ComboBox<Cinema> cinemaCombobox, cinemaComboBox;
    @FXML
    private ComboBox<Performance> performanceCombobox;

    @FXML
    private HBox currencyHBox;

    @FXML
    private DatePicker fromDatePicker, toDatePicker;

    @FXML
    private RadioButton likeSearch;
    @FXML
    private TextField movieTitleSearchText, editCinemaCapacity, editCinemaCity, editCinemaName,
            newCinemaName, newCinemaCity, newCinemaCapacity, mnbFileName;
    @FXML
    private Button deletePerformanceBtn, editCinemaBtn, saveCinemaBtn;

    @FXML
    private Label pLabel1, pLabel2;

    @FXML
    private MenuItem mnb1, mnb2;

    @FXML
    private TableColumn<Item, String> columnName, columnValue;

    private CheckComboBox<String> checkComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hideAllPane();
        this.executor = Executors.newScheduledThreadPool(2);
    }

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

    /**
     * A paraméterül kapott felületet jeleníti meg, amíg a többi felületet láthatatlanná teszi
     */
    private <T extends Region> void changeView(final T selectedPane) {
        this.hideAllPane();
        selectedPane.setVisible(true);
    }

    private void hideAllPane() {
        List.of(deletePerformancePane, editMoviePanel, createMoviePanel, accountInfoPanel, parallelPanel, mnbFilterPane)
                .forEach(p -> p.setVisible(false));
        List.of(menu1ScrollPane, menu2ScrollPane).forEach(p -> p.setVisible(false));
    }

    /**
     * 1. feladat - Adatbázis menü - Olvas2 almenü
     */
    @FXML
    protected void dbReadMenuItem2() {
        this.changeView(menu2ScrollPane);

        this.renderTableData(performanceTable_menu2);
        cinemaCombobox.setItems(FXCollections.observableArrayList(cinemaService.listAllCinema()));
        clearSearchPerformances();
    }

    /**
     * 1. feladat - Adatbázis menü - Ír almenü
     */
    @FXML
    public void dbCreateMenuItem() {
        this.changeView(createMoviePanel);
        this.setStringChangeListener(newCinemaRegListener, this.newCinemaCity, newCinemaName, newCinemaCapacity);
        onlyNumbersEventListener(newCinemaCapacity);
        cleanNewPage();
    }

    /**
     * 1. feladat - Adatbázis menü - Módosít almenü
     */
    @FXML
    public void dbEditMenuItem() {
        this.changeView(editMoviePanel);
        this.setStringChangeListener(stringChangeListener, this.editCinemaCity, this.editCinemaName, this.editCinemaCapacity);
        onlyNumbersEventListener(editCinemaCapacity);
        this.refreshCinemaCombo(cinemaComboBox);
    }

    /**
     * 1. feladat - Adatbázis menü - Töröl almenü
     */
    @FXML
    public void dbDeleteMenuItem() {
        this.changeView(deletePerformancePane);

        performanceCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deletePerformanceBtn.setDisable(newValue == null);
        });
        this.setPerformanceCombobox();
    }

    /**
     * 1. feladat - Adatbázis menü - Töröl almenü - Kattintás álltal kiváltott esemény
     */
    @FXML
    public void deletePerformance() {
        Performance selectedItem = performanceCombobox.getSelectionModel().getSelectedItem();
        performanceService.deletePerformance(selectedItem);
        this.setPerformanceCombobox();
    }

    @FXML
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

    @FXML
    public void cinemaSelected(ActionEvent actionEvent) {
        final Cinema selectedCinema = this.cinemaComboBox.getSelectionModel().getSelectedItem();

        final String city = ofNullable(selectedCinema).map(Cinema::getCity).orElse(null);
        final String cinemaName = ofNullable(selectedCinema).map(Cinema::getName).orElse(null);
        final String maxCapacity = ofNullable(selectedCinema).map(c -> String.valueOf(c.getMaxCapacity())).orElse(null);

        this.editCinemaCity.setText(city);
        this.editCinemaName.setText(cinemaName);
        this.editCinemaCapacity.setText(maxCapacity);

        boolean isDisabled = isNotEmpty(cinemaName) && isNotEmpty(maxCapacity) && isNotEmpty(city);
        editCinemaBtn.setDisable(!isDisabled);
    }

    private void validateForm() {
        final Cinema selectedCinema = this.cinemaComboBox.getSelectionModel().getSelectedItem();
        if (isNull(selectedCinema)) {
            editCinemaBtn.setDisable(true);
            return;
        }

        boolean isModified = !selectedCinema.getName().equals(editCinemaName.getText())
                || !selectedCinema.getCity().equals(editCinemaCity.getText())
                || !String.valueOf(selectedCinema.getMaxCapacity()).equals(editCinemaCapacity.getText());

        boolean isNameValid = isNotEmpty(editCinemaName.getText());
        boolean isCityValid = isNotEmpty(editCinemaCity.getText());
        boolean isCapacityValid = isNotEmpty(editCinemaCapacity.getText()) && editCinemaCapacity.getText().matches(NUMBER_REGEX);

        editCinemaBtn.setDisable(!(isModified && isNameValid && isCityValid && isCapacityValid));
    }

    private void newCinemaRegValidator() {
        boolean isNameValid = isNotEmpty(newCinemaName.getText());
        boolean isCityValid = isNotEmpty(newCinemaCity.getText());
        boolean isCapacityValid = isNotEmpty(newCinemaCapacity.getText()) && newCinemaCapacity.getText().matches(NUMBER_REGEX);

        saveCinemaBtn.setDisable(!(isNameValid && isCityValid && isCapacityValid));
    }

    @FXML
    public void editCinema(ActionEvent actionEvent) {
        final Cinema selectedCinema = this.cinemaComboBox.getSelectionModel().getSelectedItem();
        if (isNull(selectedCinema)) {
            return;
        }

        selectedCinema.setCity(this.editCinemaCity.getText());
        selectedCinema.setName(this.editCinemaName.getText());
        selectedCinema.setMaxCapacity(Integer.parseInt(this.editCinemaCapacity.getText()));

        alert(SUCCESSFULLY_SAVED, SUCCESSFULLY_SAVED_MSG, () -> cinemaService.saveCinema(selectedCinema));
        this.cleanEditPage();
    }

    private void cleanEditPage() {
        this.refreshCinemaCombo(cinemaComboBox);
        cinemaCombobox.getSelectionModel().clearSelection();
        List.of(this.editCinemaName,
                this.editCinemaCity,
                this.editCinemaCapacity
        ).forEach(TextInputControl::clear);
    }

    private void cleanNewPage() {
        List.of(this.newCinemaName,
                this.newCinemaCity,
                this.newCinemaCapacity
        ).forEach(f -> f.setText(null));
    }

    /**
     * Új mozi hozzáadása
     */
    @FXML
    public void saveCinema(ActionEvent actionEvent) {
        Cinema cinema = new Cinema();
        cinema.setMaxCapacity(Integer.parseInt(this.newCinemaCapacity.getText()));
        cinema.setCity(this.newCinemaCity.getText());
        cinema.setName(this.newCinemaName.getText());

        alert(SUCCESSFULLY_SAVED, SUCCESSFULLY_SAVED_MSG, () -> cinemaService.saveCinema(cinema));
        this.cleanNewPage();
    }

    /**
     * 2. feladat - SOAP kliens - letölt1 almenü
     */
    @FXML
    private void mnbLetolt() {
        mnbService.downloadAll(
                mnb1.getParentPopup().getScene().getWindow()
        );
    }

    /**
     * 2. feladat - SOAP kliens - letölt2 almenü
     */
    @FXML
    public void mnbLetolt2(ActionEvent actionEvent) {
        this.changeView(this.mnbFilterPane);
        this.checkComboBox = new CheckComboBox<>();
        checkComboBox.getItems().addAll(mnbService.currencies());

        currencyHBox.getChildren().remove(1);
        currencyHBox.getChildren().add(checkComboBox);
    }

    /**
     * 2. feladat - SOAP kliens - letölt2 almenü - Letöltés gomb action
     */
    public void downloadMNB2(ActionEvent actionEvent) {
        final ObservableList<String> selectedCurrencies = checkComboBox.getCheckModel().getCheckedItems();
        final String text = mnbFileName.getText();

        mnbService.downloadByInputs(
                mnb2.getParentPopup().getScene().getWindow(),
                text,
                this.fromDatePicker.getValue(),
                this.toDatePicker.getValue(),
                selectedCurrencies
        );
    }

    /**
     * 4. feladat - Forex menü (Oanda API)- Számlainformációk almenü
     */
    @FXML
    public void oandaAccountInfo(ActionEvent actionEvent) {
        this.changeView(accountInfoPanel);
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnValue.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        accountTableView.setItems(FXCollections.observableArrayList(oandaService.accountInformation()));
    }

    @FXML
    private void oandaPrices(ActionEvent actionEvent) {
    }

    @FXML
    private void oandaPriceHistory(ActionEvent actionEvent) {
    }

    @FXML
    private void oandaOpening(ActionEvent actionEvent) {
    }

    @FXML
    private void oandaClosing(ActionEvent actionEvent) {

    }

    @FXML
    private void oandaOpenPositions(ActionEvent actionEvent) {
//           performanceTable_menu1.setItems(
//                   FXCollections.observableArrayList( oandaService.listOpenPositions())
//           );
    }

    @FXML
    private void parallelProgramming(ActionEvent actionEvent) {
        this.changeView(parallelPanel);
    }

    /**
     * 3. feladat - Párhuzamos programozás
     */
    @FXML
    private void startParallel() {
        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> pLabel1.setText("Label 1: " + UUID.randomUUID()));
        }, 0, 1, TimeUnit.SECONDS);

        executor.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> pLabel2.setText("Label 2: " + UUID.randomUUID()));
        }, 0, 2, TimeUnit.SECONDS);
    }

    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private void setPerformanceCombobox() {
        performanceCombobox.setItems(
                FXCollections.observableArrayList(performanceService.listPerformances())
        );
    }

    private void refreshCinemaCombo(ComboBox<Cinema> comboBox) {
        comboBox.setItems(
                FXCollections.observableArrayList(cinemaService.listAllCinema())
        );
    }

    private void setStringChangeListener(ChangeListener<String> listener, TextField... tfs) {
        Arrays.stream(tfs)
                .forEach(tf -> tf.textProperty().addListener(listener));
    }

}