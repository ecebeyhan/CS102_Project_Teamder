package scenes;

import classes.Database;
import classes.Match;
import classes.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class friendController implements MainController, Initializable {

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

    public void clickOnBack(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder | Profile Page", sc.loggedInUser, new profileController());
    }

    @Override
    public void preloadData(User user) throws IOException {
        this.user = user;
        userNameLabel.setText(user.getName());
        sportsLabel.setText(user.getSports());
        bioText.setText(user.getBio());
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        matchNameColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("matchLink"));
        joinedMNameColumn.setCellValueFactory( new PropertyValueFactory<Match, String>("name"));
        rateColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("rateLink"));
    }
}
