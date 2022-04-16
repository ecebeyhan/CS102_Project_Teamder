package scenes;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

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

    @FXML
    protected void clickOnLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement myStatement = null;
        ResultSet resultSet = null;

        String uName = userNameTField.getText();
        String uPass = passTField.getText();

        if(uName.length() < 1 || uPass.length() < 1) {
            errorLabel.setText("Enter a valid input"); return;
        }

        try {
            String url = "jdbc:postgresql://rogue.db.elephantsql.com:5432/iepnsbnu";
            String username = "iepnsbnu";
            String password = "RucLTf_zMlhMaa99HMxypHICcednwQix";

            // 1 connect to db
            conn = DriverManager.getConnection(url, username, password);

            // 2 create a query ( with ? for user input)
            String query = "SELECT * FROM users WHERE name = ?";

            // 3 prepare the statement wanted to run on the sql
            myStatement = conn.prepareStatement(query);

            // 4 establish the '?' created in 'step 2'
            myStatement.setString(1, uName);

            // 5 execute the query
            resultSet = myStatement.executeQuery();

            // 6 get passwords from the db compare them with userNames
            String dbPassword = null;
            String sports = null;
            String bio = null;
            User user = null;

            while (resultSet.next()) {

                dbPassword = resultSet.getString("password");

                sports = resultSet.getString("sports");

                bio = resultSet.getString("bio");

                user = new User(uName,
                        resultSet.getString("password"),
                        resultSet.getString("sports"),
                        resultSet.getString("bio"));
                user.setUserID(resultSet.getInt("userid"));

            }

            SceneChanger sc = new SceneChanger();

            if(uPass.equals(dbPassword)) {
                // SceneChanger.setLoggedInUser(user);
                errorLabel.setText("Logged in successfully!");
            }
            else
                //if the user DNE
                errorLabel.setText("There is no such a user!");


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void clickOnClear(ActionEvent event) {
        userNameTField.setText("");
        passTField.setText("");
        errorLabel.setText("Inputs are cleaned!");
    }

    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Welcome_To_Teamder.fxml", "Welcome Page");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
    }
}