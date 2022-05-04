package scenes;

import classes.Database;
import classes.ImageHandler;
import classes.Match;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class friendController implements MainController, Initializable {

    private User user;
    private Database database;
    @FXML
    private Button followButton;
    @FXML
    private Button unfollowButton;
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

    @FXML
    private TableView<Match> joinedMatchTable;
    @FXML
    private TableColumn<Match, String> joinedMNameColumn;


    @FXML
    private TableView<User> friendListTable;
    @FXML
    private TableColumn<User, String> friendListColumn;
    private ObservableList<User> userObservableList;

    private File imageFile;

    public void clickOnBack(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder | Profile Page", SceneChanger.loggedInUser, new profileController());
    }
    public void clickOnFollow(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
        database.insertFriend(user, SceneChanger.loggedInUser);
        database.insertFriend(SceneChanger.loggedInUser, user);
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder | Profile Page", SceneChanger.loggedInUser, new profileController());
    }
    public void clickOnUnfollow(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
        database.removeFriend(user, SceneChanger.loggedInUser);
        database.removeFriend(SceneChanger.loggedInUser, user);
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder | Profile Page", SceneChanger.loggedInUser, new profileController());
    }

    @Override
    public void preloadData(User user) throws IOException {
        this.user = user;
        this.database = new Database();
        userNameLabel.setText(user.getName());
        sportsLabel.setText(user.getSports());
        bioText.setText(user.getBio());
        avgLabel.setText(String.valueOf(user.getRating()) + "/5");

        try {
            userObservableList = database.getFriends(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean alreadyFriend = false;
        for(int i = 0; i < userObservableList.size(); i++){
            if(userObservableList.get(i).getUserName().equals(SceneChanger.loggedInUser.getUserName())){
                alreadyFriend = true;
                break;
            }
        }
        if(alreadyFriend){
            followButton.setVisible(false);
            unfollowButton.setVisible(true);
        }
        else{
            followButton.setVisible(true);
            unfollowButton.setVisible(false);
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

        ObservableList<Match> currentMatches = null;
        ObservableList<Match> joinedMatches = null;
        try {
            currentMatches = Database.getActiveMatches(user);
            joinedMatches = Database.getInactiveMatches(user);
        } catch (SQLException e) {
            e.getStackTrace();
        }
        assert currentMatches != null;
        currentMatchTable.getItems().addAll(currentMatches);
        assert joinedMatches != null;
        joinedMatchTable.getItems().addAll(joinedMatches);
        createCurrentMLinks();

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

    private void createCurrentMLinks() {
        ObservableList<Hyperlink> links = FXCollections.observableArrayList();
        for (int i = 0; i < currentMatchTable.getItems().size(); i++) {
            Match m = currentMatchTable.getItems().get(i);
            TableColumn<Match, ?> col = currentMatchTable.getColumns().get(0);
            Hyperlink link = (Hyperlink) col.getCellObservableValue(m).getValue();
            links.add(link);
        }
        for (Hyperlink link : links) {
            link.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    SceneChanger sc = new SceneChanger();
                    try {
                        Match match = Database.getMatch(((Hyperlink) t.getSource()).getText());
                        sc.changeScenes(t, "Match_Page.fxml", "Teamder | Match Page", SceneChanger.getLoggedInUser(), match, new matchPageController(), new matchPageController());
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }




    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        matchNameColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("matchLink"));
        joinedMNameColumn.setCellValueFactory( new PropertyValueFactory<Match, String>("name"));
        friendListColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
    }

}
