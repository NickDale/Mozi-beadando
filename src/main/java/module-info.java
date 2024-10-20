module hu.nje.mozifxml {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.slf4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens hu.nje.mozifxml to javafx.fxml;
    opens hu.nje.mozifxml.db.entities to org.hibernate.orm.core;

    exports hu.nje.mozifxml;
}