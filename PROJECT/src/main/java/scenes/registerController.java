package scenes;

import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;


public class registerController {

    @FXML
    private Label label;
    @FXML
    private Label label2;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
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
        if (!(nameTextField.getText().length() > 0)) {
            label.setText("Enter a valid username!");
            footballCB.setSelected(false);
            basketCB.setSelected(false);
            tennisCB.setSelected(false);
            volleyballCB.setSelected(false);
            passwordField.clear();
            bioTextArea.clear();
            return;
        }
        if (!(passwordField.getText().length() >0)) {
            label.setText("Enter a valid password!");
            footballCB.setSelected(false);
            basketCB.setSelected(false);
            tennisCB.setSelected(false);
            volleyballCB.setSelected(false);
            nameTextField.clear();
            bioTextArea.clear();
            return;
        }
        if (!(bioTextArea.getText().length() > 0)) {
            label.setText("Enter a valid bio!");
            footballCB.setSelected(false);
            basketCB.setSelected(false);
            tennisCB.setSelected(false);
            volleyballCB.setSelected(false);
            nameTextField.clear();
            passwordField.clear();
            return;
        }


        if ( !(footballCB.isSelected() || tennisCB.isSelected() || basketCB.isSelected() || volleyballCB.isSelected())) {
            label.setText("Select (a) sport(s)!");
            return;
        }
        else {
            createUser();
            label.setText("Account has been created!");
            label2.setText(" Click on 'Cancel' to return Login Page.");
        }
    }

    @FXML
    protected void clickCancelButton(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Welcome_To_Teamder.fxml", "Welcome Page");
    }

    public void createUser() throws IOException, SQLException {
        String sports = "";
        if (footballCB.isSelected()) { sports += "Football, "; }
        if (basketCB.isSelected()) { sports += "Basketball, "; }
        if (tennisCB.isSelected()) { sports += "Tennis, "; }
        if (volleyballCB.isSelected()) { sports += "Volleyball, "; }
        if (sports.length() > 0) { sports = sports.substring(0,sports.length()-2); }
        user = new User(nameTextField.getText(), passwordField.getText(), sports,bioTextArea.getText());

        user.insertUserToDB();

    }
}