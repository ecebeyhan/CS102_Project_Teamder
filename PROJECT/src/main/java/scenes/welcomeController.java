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

    /**
     * This method is called when the create account button is clicked.
     * It opens the Create Account scene. (Make_Profile.fxml)
     * @param event
     */
    @FXML
    protected void clickOnCreateButton(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Make_Profile.fxml", "Teamder | Create Account");
    }

    /**
     * This method is called when the login button is clicked.
     * It opens the Login scene. (Login.fxml)
     * @param event
     */
    @FXML
    protected void clickOnLoginButton(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Log_In.fxml", "Teamder | Login");
    }
}
