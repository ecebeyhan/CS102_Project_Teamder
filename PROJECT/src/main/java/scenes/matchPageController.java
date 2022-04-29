package scenes;

import classes.Match;
import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class matchPageController implements MatchController, MainController{

    @FXML
    private Text placeText;
    @FXML
    private Text timeText;
    @FXML
    private Text dateText;

    public void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }

    @Override
    public void preLoadMatch(Match match) throws IOException {
        placeText.setText(match.getName());
        timeText.setText(match.getStartTime().toString());
        dateText.setText(match.getDate().toString());

    }

    @Override
    public void preloadData(User user) throws IOException {

    }
}
