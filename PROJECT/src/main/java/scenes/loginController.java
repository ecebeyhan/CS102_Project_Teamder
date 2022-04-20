package scenes;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

import classes.Database;
import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController implements Initializable {
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

    /**
     * This method is used to login the user
     * First it connects to the database
     * Then it checks whether the user with the specified username exists
     * If it does, it checks whether the password is correct
     * If it is, it logs in the user
     * Finally, opens the profile page with the user's information
     * @param event
     */
    @FXML
    protected void clickOnLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
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
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Welcome_To_Teamder.fxml", "Teamder | Welcome Page");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
    }
}