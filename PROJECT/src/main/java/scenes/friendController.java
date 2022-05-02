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

    @FXML
    private TableView<Match> joinedMatchTable;
    @FXML
    private TableColumn<Match, String> joinedMNameColumn;
    @FXML
    private TableColumn<Match, Hyperlink> rateColumn;


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

        /*ObservableList<Match> currentMatches = null;
        ObservableList<Match> joinedMatches = null;
        try {
            //Database methods to create currentMatches, joinedMatches
        } catch (SQLException e) {
            e.getStackTrace();
        }
        currentMatchTable.getItems().addAll(currentMatches);
        joinedMatchTable.getItems().addAll(joinedMatches);
        createCurrentMLinks();
        createRateLinks();
        */
    }

    /*private void createCurrentMLinks(){
        ObservableList<Hyperlink> links = FXCollections.observableArrayList();
        for( int i = 0; i < currentMatchTable.getItems().size(); i++){
            Match m = currentMatchTable.getItems().get(i);
            TableColumn col = currentMatchTable.getColumns().get(0);
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
                        MainController MatchPage = new matchPageController();
                        MatchController userPage = new matchPageController();
                        sc.changeScenes(t, "Match_Page.fxml", "Teamder | Match Page", SceneChanger.getLoggedInUser(), match, userPage, MatchPage);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }




    }

    private void createRateLinks(){
        ObservableList<Hyperlink> links = FXCollections.observableArrayList();
        ObservableList<Match> matches = FXCollections.observableArrayList();
        for( int i = 0; i < joinedMatchTable.getItems().size(); i++){
            Match m = joinedMatchTable.getItems().get(i);
            matches.add(m);
            TableColumn col = joinedMatchTable.getColumns().get(1);
            Hyperlink link = (Hyperlink) col.getCellObservableValue(m).getValue();
            links.add(link);
        }
        for (int j = 0; j < joinedMatchTable.getItems().size(); j++) {
            int finalJ = j;
            links.get(j).setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    SceneChanger sc = new SceneChanger();
                    try {
                        Match match = Database.getMatch(matches.get(finalJ).getName());
                        MainController userPage = new ratePageController();
                        MatchController matchPage = new ratePageController();
                        sc.changeScenes(t, "Rate.fxml", "Teamder | Rate Page", SceneChanger.getLoggedInUser(), match, matchPage, userPage);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }*/



    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        matchNameColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("matchLink"));
        joinedMNameColumn.setCellValueFactory( new PropertyValueFactory<Match, String>("name"));
        rateColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("rateLink"));
    }
}
