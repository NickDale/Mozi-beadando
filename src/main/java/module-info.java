module hu.nje.mozifxml {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.slf4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jakarta.annotation;
    requires jakarta.xml.bind;

    opens hu.nje.mozifxml to javafx.fxml;
    opens hu.nje.mozifxml.entities to org.hibernate.orm.core;

    exports hu.nje.mozifxml;
    exports hu.nje.mozifxml.controller.model;
    exports hu.nje.mozifxml.controller;
    exports hu.nje.mozifxml.entities;
    opens hu.nje.mozifxml.controller to javafx.fxml;
    exports hu.nje.mozifxml.util;
    opens hu.nje.mozifxml.util to javafx.fxml;
}