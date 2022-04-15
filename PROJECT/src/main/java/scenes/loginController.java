package scenes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {
    @FXML
    private TextField userNameTField;
    @FXML
    private PasswordField passTField;
    @FXML
    private Button clearButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;

    @FXML
    protected void clickOnLogin(ActionEvent event) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        
    }

    @FXML
    protected void clickOnClear(ActionEvent event) {
        userNameTField.setText("");
        passTField.setText("");
        errorLabel.setText("Inputs are cleaned!");
    }
}