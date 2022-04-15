package scenes;

import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;


public class registerController {
    @FXML
    private Button registerButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea bioTextArea;
    @FXML
    private RadioButton footballCB;
    @FXML
    private RadioButton basketCB;
    @FXML
    private RadioButton tennisCB;
    @FXML
    private RadioButton volleyballCB;
    @FXML
    private PasswordField passwordField;

    private User user;



    @FXML
    protected void clickRegisterButton(ActionEvent event) throws IOException, SQLException {
        createUser();
    }

    public void createUser() throws IOException, SQLException {
        String sports = "";
        if (footballCB.isSelected()) { sports += "Football,"; }
        if (basketCB.isSelected()) { sports += "Basketball,"; }
        if (tennisCB.isSelected()) { sports += "Tennis,"; }
        if (volleyballCB.isSelected()) { sports += "Volleyball,"; }
        if (sports.length() > 0) { sports = sports.substring(0,sports.length()-1); }
        user = new User(nameTextField.getText(), passwordField.getText(), sports,bioTextArea.getText());

        user.insertUserToDB();

    }
}