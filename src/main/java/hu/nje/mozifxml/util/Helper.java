package hu.nje.mozifxml.util;

import com.oanda.v20.primitives.DateTime;
import hu.nje.mozifxml.entities.AbstractEntity;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static hu.nje.mozifxml.util.Constant.ERROR_MSG;
import static hu.nje.mozifxml.util.Constant.ERROR_TITLE;
import static hu.nje.mozifxml.util.Constant.NUMBER_REGEX;
import static hu.nje.mozifxml.util.Constant.WARNING_TITLE;

public class Helper {
    public static final String CONFIG_FILE_NAME = "config.properties";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
    public static final Function<DateTime, String> format = dateString ->
            Helper.DATE_TIME_FORMATTER.format(Instant.parse(dateString.toString()));

    public static final Function<String, Alert> warningAlertPopup = (msg) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WARNING_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        return alert;
    };
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

    public static Properties loadConfigProperties() {
        try (InputStream input = Helper.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (input == null) {
                throw new RuntimeException("Missing config.properties file!");
            }

            final var properties = new Properties();
            properties.load(input);

            return properties;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
