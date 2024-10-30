package hu.nje.mozifxml.util;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import static hu.nje.mozifxml.util.Constant.NUMBER_REGEX;

public class Helper {

    public static void onlyNumbersEventListener(TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches(NUMBER_REGEX)) {
                keyEvent.consume();
            }
        });
    }

}
