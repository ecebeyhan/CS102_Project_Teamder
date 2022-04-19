package scenes;

import classes.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class profileController implements MainController, Initializable {

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
    private boolean imgChanged;
    private User user;

    /**
     * This method is called when the user clicks on the Log Out button.
     * Simply goes back to Login page.
     * @param event the event that triggers the method
     */
    @FXML
    protected void clickOnLogOut(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Log_In.fxml", "Login Page");
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
    protected void clickOnEditImage(ActionEvent event) {
        // get the stage to open a new window to select an image
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // initialize the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");

        // set the extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg, *.png)", "*.jpg", "*.png");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
        fileChooser.getExtensionFilters().addAll(extFilter, extFilter2);

        // set the initial directory
        String userDirectoryString = System.getProperty("user.home");  // it works for windows as far as I know
        File userDirectory = new File(userDirectoryString);
        fileChooser.setInitialDirectory(userDirectory);

        // open the file dialog
        File tmpImageFile = fileChooser.showOpenDialog(stage);

        if (tmpImageFile != null) {
            imageFile = tmpImageFile;

            // update the image view
            if (imageFile.isFile()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(image);
                    imgChanged = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method preloads the profile page with the user's information.
     * @param user the user to be loaded
     */
    @Override
    public void preloadData(User user) {
        this.user = user;
        userNameLabel.setText(user.getName());
        sportsLabel.setText(user.getSports());
        bioText.setText(user.getBio());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imgChanged = false;

        // load the default image to profile image view
        try {
            imageFile = new File("./src/main/java/images/defaultPerson.png");
            BufferedImage img = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(img, null);
            imageView.setImage(image);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}
