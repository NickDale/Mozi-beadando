package hu.nje.mozifxml.connection;


import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:mozi.db";
    private Connection connection;

    public void connect() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(DB_URL);
            }
        } catch (Exception ex) {
            System.err.println("ex = " + ex);
        }
    }
}
