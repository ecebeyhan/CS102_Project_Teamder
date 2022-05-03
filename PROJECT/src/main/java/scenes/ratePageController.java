package scenes;

import classes.Match;
import classes.User;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ratePageController implements MainController, MatchController {
    @Override
    public void preloadData(User user) throws IOException {

    }

    @Override
    public void preLoadMatch(Match match) throws IOException {

    }

    public void clickOnBack(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
    }
}
