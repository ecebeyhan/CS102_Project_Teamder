package scenes;

import classes.Match;
import classes.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ratePageController implements MainController, MatchController, Initializable {

    @FXML
    private ComboBox<Integer> rate1, rate2, rate3, rate4, rate5;

    public void clickOnRate(ActionEvent event) throws IOException {

    }
    public void clickOnBack(ActionEvent event) throws IOException {
        new SceneChanger().changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rate1.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        rate2.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        rate3.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        rate4.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        rate5.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
    }

    @Override
    public void preloadData(User user) throws IOException {

    }

    @Override
    public void preLoadMatch(Match match) throws IOException {

    }
}
