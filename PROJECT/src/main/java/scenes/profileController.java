package scenes;

import classes.ImageHandler;
import classes.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class profileController implements MainController  {

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
    @FXML
    private ImageView imageView;

    private File imageFile;
    private User user;

    /**
     * This method is called when the user clicks on the Log Out button.
     * Simply goes back to Login page.
     * @param event the event that triggers the method
     */
    @FXML
    protected void clickOnLogOut(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Log_In.fxml", "Teamder | Login Page");
    }

    /**
     * This method is called when the user clicks on the Join a Match button.
     * It will open a new window to allow the user to join a match. (*.fxml)
     * @param event the event that triggers the method
     */
    @FXML
    protected void clickOnJoin(ActionEvent event) {

    }

    /**
     * This method is called when the user clicks on the Start a Match button.
     * It will open a new window to allow the user to start a match. (Match_Page.fxml)
     * @param event the event that triggers the method
     */
    @FXML
    protected void clickOnStart(ActionEvent event) {

    }

    /**
     * This method launch a FileChooser to select an image file.
     * When this is done, the image is loaded to the profile image view.
     * @param event the event that triggers this method
     */
    @FXML
    protected void clickOnEditImage(ActionEvent event) throws IOException, SQLException {
        ImageHandler.editImage(event, imageFile, imageView, user);
    }

    /**
     * This method preloads the profile page with the user's information.
     * @param user the user to be loaded
     */
    @Override
    public void preloadData(User user) throws IOException {
        this.user = user;
        userNameLabel.setText(user.getName());
        sportsLabel.setText(user.getSports());
        bioText.setText(user.getBio());

        try{
            imageFile = new File(ImageHandler.IMAGE_PATH + user.getImageFile());
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(img);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
