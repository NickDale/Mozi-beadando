package hu.nje.mozifxml.util;

import hu.nje.mozifxml.entities.AbstractEntity;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static hu.nje.mozifxml.util.Constant.ERROR_MSG;
import static hu.nje.mozifxml.util.Constant.ERROR_TITLE;
import static hu.nje.mozifxml.util.Constant.NUMBER_REGEX;

public class Helper {

    private static final BiFunction<String, String, Alert> infoAlert = (title, msg) -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        return alert;
    };

    private static final Supplier<Alert> errorAlert = () -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ERROR_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(ERROR_MSG);
        return alert;
    };

    public static void onlyNumbersEventListener(TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches(NUMBER_REGEX)) {
                keyEvent.consume();
            }
        });
    }

    public static <T extends AbstractEntity> void alert(final String title, final String msg, Callable<Boolean> callable) {
        try {
            Boolean call = callable.call();
            Alert alert;
            if (Boolean.TRUE.equals(call)) {
                alert = infoAlert.apply(title, msg);
            } else {
                alert = errorAlert.get();
            }
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = errorAlert.get();
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}
