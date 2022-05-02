package scenes;

import classes.Database;
import classes.ImageHandler;
import classes.Match;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;
import javafx.scene.text.Text;
import javafx.scene.control.Labeled;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class profileController implements MainController, Initializable  {

    private Database database;
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
    @FXML
    private TableView<Match> currentMatchTable;
    @FXML
    private TableColumn<Match, Hyperlink> matchNameColumn;
    private ObservableList<Hyperlink> currentMatchLinks;
    @FXML
    private TableView<Match> joinedMatchTable;
    @FXML
    private TableColumn<Match, String> joinedMNameColumn;
    @FXML
    private TableColumn<Match, Hyperlink> rateColumn;
    private ObservableList<Hyperlink> rateLinks;

    @FXML
    private TableView<User> friendListTable;
    @FXML
    private TableColumn<User, String> friendListColumn;
    private ObservableList<User> userObservableList;

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
    protected void clickOnJoin(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new FindMatchController();
        sc.changeScenes(event, "Find_aMatch.fxml", "Teamder | Find a Match Page");
    }

    /**
     * This method is called when the user clicks on the Start a Match button.
     * It will open a new window to allow the user to start a match. (Match_Page.fxml)
     * @param event the event that triggers the method
     */
    @FXML
    protected void clickOnStart(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new createMatchController();
        sc.changeScenes(event, "Create_Match_Page.fxml", "Teamder | Create Match Page");

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
    public void preloadData(User user) {
        this.database = new Database();
        this.user = user;
        userNameLabel.setText(user.getName());
        sportsLabel.setText(user.getSports());
        bioText.setText(user.getBio());

        try {
            userObservableList = database.getFriends(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        friendListTable.setItems(userObservableList);
        friendListTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
                SceneChanger sc = new SceneChanger();
                try {
                    String username = friendListTable.getSelectionModel().getSelectedItem().getUserName();
                    User friend = database.getUser(username);
                    sc.changeScenes(actionEvent, "Friend_Page.fxml", "Teamder | Friend", friend, new friendController());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

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

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        matchNameColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("matchLink"));
        joinedMNameColumn.setCellValueFactory( new PropertyValueFactory<Match, String>("name"));
        rateColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("rateLink"));
        friendListColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
    }
}
