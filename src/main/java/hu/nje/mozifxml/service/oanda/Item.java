package hu.nje.mozifxml.service.oanda;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {

    private final SimpleStringProperty name;
    private final SimpleStringProperty value;

    public Item(String name, Object value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value == null ? "" : value.toString());
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }
}
