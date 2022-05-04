package scenes;

import java.io.IOException;
import java.sql.*;

import classes.Database;
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
    private Button cancelButton;
    @FXML
    private Label errorLabel;


    @FXML
    protected void clickOnLogin(ActionEvent event) throws IOException, SQLException {
        Database database = new Database();
        String uName = userNameTField.getText();
        String uPass = passTField.getText();

        if(uName.equals("") || uPass.equals("")) {
            errorLabel.setText("Please fill in all the inputs!");
        }
        else {
            if(!Database.login(uName, uPass, event)){
                errorLabel.setText("Wrong username or password!");
            }
        }
    }

    /**
     * This method is used to clear the text fields
     * @param event
     */
    @FXML
    protected void clickOnClear(ActionEvent event) {
        userNameTField.setText("");
        passTField.setText("");
        errorLabel.setText("Inputs are cleaned!");
    }

    /**
     * This method is used to cancel the login
     * Simply goes back to Welcome Page
     * @param event
     */
    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException {
        new SceneChanger().changeScenes(event, "Welcome_To_Teamder.fxml", "Teamder | Welcome Page");
    }

}