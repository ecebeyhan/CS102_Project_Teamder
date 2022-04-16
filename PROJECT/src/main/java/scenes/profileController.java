package scenes;

import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;

public class profileController implements MainController{

    @FXML
    private Button logOutButton;
    @FXML
    private Button joinButton;
    @FXML
    private Button startButton;
    @FXML
    private Button imageButton;
    @FXML
    private Text userNameLabel;
    @FXML
    private Text sportsLabel;
    @FXML
    private Text avgLabel;
    @FXML
    private TextArea bioText;

    private User user;

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


    @Override
    public void preloadData(User user) {
        this.user = user;
        userNameLabel.setText(user.getName());
        sportsLabel.setText(user.getSports());
        bioText.setText(user.getBio());

    }
}
