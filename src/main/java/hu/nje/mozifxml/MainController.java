package hu.nje.mozifxml;

import hu.nje.mozifxml.db.service.PerformanceService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    private final PerformanceService performanceService = new PerformanceService();
    @FXML
    private Label welcomeText;

    @FXML
    protected void onClickExit() {
        System.exit(0);
    }

    @FXML
    protected void dbReadMenuItem() {
        performanceService.listPerformances();
    }

    @FXML
    protected void dbReadMenuItem2() {
        performanceService.listPerformancesByFilter("dsd", 1L, true);
    }
}