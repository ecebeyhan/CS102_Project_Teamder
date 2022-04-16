package scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class welcomeController {

    @FXML
    private Button createAccButton;
    @FXML
    private Button loginButton;


    @FXML
    protected void clickOnCreateButton(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Make_Profile.fxml", "Create Account");
    }

    @FXML
    protected void clickOnLoginButton(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Log_In.fxml", "Login");
    }
}
