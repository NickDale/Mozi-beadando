module hu.nje.mozifxml {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.slf4j;

    opens hu.nje.mozifxml to javafx.fxml;
    exports hu.nje.mozifxml;
}