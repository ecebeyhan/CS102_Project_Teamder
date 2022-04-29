module com.example.teamder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;


    opens scenes to javafx.fxml;
    opens classes to javafx.fxml;
    exports scenes;
    exports classes;
}