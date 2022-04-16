package scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class profileController {

    @FXML
    private Button logOutButton;
    @FXML
    private Button joinButton;
    @FXML
    private Button startButton;
    @FXML
    private Button imageButton;

    @FXML
    protected void clickOnLogOut(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Log_In.fxml", "Login Page");
    }

    @FXML
    protected void clickOnJoin(ActionEvent event) {

    }

    @FXML
    protected void clickOnStart(ActionEvent event) {

    }

    @FXML
    protected void clickOnAddImage(ActionEvent event) {

    }


}
