module com.example.teamder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens scenes to javafx.fxml;
    exports scenes;
}