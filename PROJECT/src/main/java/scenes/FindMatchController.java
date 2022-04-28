package scenes;

import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;

public class FindMatchController implements MainController
{


    @Override
    public void preloadData(User volunteer) throws IOException {
        return;
    }

    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
//        sc.changeScenes(event,"Profile_Page.fxml", "Teamder | Profile Page");
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }

    @FXML
    void clickSearchButton(ActionEvent event)
    {

    }



}
