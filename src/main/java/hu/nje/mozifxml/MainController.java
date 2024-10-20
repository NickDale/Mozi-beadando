package hu.nje.mozifxml;

import hu.nje.mozifxml.db.MovieService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;

   private final MovieService movieService = new MovieService();

    @FXML
    protected void onClickExit() {
        System.exit(0);
    }

    @FXML
    protected void dbReadMenuItem(){
        movieService.createUser("","");
    }

    @FXML
    protected void dbReadMenuItem2(){

    }
}