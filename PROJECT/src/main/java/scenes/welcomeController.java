package scenes;

import classes.ImageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class welcomeController implements Initializable {

    @FXML
    private Button createAccButton;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView footballImage, basketballImage, tennisImage, volleyballImage, logoImage;

    /**
     * This method is called when the create account button is clicked.
     * It opens the Create Account scene. (Make_Profile.fxml)
     * @param event
     */
    @FXML
    protected void clickOnCreateButton(ActionEvent event) throws IOException {
        new SceneChanger().changeScenes(event, "Make_Profile.fxml", "Teamder | Create Account");
    }

    /**
     * This method is called when the login button is clicked.
     * It opens the Login scene. (Login.fxml)
     * @param event
     */
    @FXML
    protected void clickOnLoginButton(ActionEvent event) throws IOException {
        new SceneChanger().changeScenes(event, "Log_In.fxml", "Teamder | Login");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            File file = new File(ImageHandler.IMAGE_PATH + "futboldobu.jpeg");
            Image image = new Image(file.toURI().toURL().toString());
            footballImage.setImage(image);
            file = new File(ImageHandler.IMAGE_PATH + "tennisdobu.jpeg");
            image = new Image(file.toURI().toURL().toString());
            tennisImage.setImage(image);
            file = new File(ImageHandler.IMAGE_PATH + "basketdobu.jpeg");
            image = new Image(file.toURI().toURL().toString());
            basketballImage.setImage(image);
            file = new File(ImageHandler.IMAGE_PATH + "volleydobu.jpeg");
            image = new Image(file.toURI().toURL().toString());
            volleyballImage.setImage(image);
            file = new File(ImageHandler.IMAGE_PATH + "icon.jpeg");
            image = new Image(file.toURI().toURL().toString());
            logoImage.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
