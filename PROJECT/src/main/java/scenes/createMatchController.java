package scenes;

import classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class createMatchController implements  MainController{

    @FXML
    private ComboBox place;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox sport;
    @FXML
    private TextField matchName;
    @FXML
    private TextField time;
    @FXML
    private RadioButton min30;
    @FXML
    private RadioButton min45;
    @FXML
    private RadioButton min60;
    @FXML
    private RadioButton min90;
    @FXML
    private Button create;
    @FXML
    private Button cancel;
    @FXML
    private Label errorLabel;


    @FXML
    protected void clickOnCreate(ActionEvent event) {
        if (matchName.getText().isEmpty() || time.getText().isEmpty() || date.getValue() == null || sport.getValue() == null || place.getValue() == null) {
            errorLabel.setText("Please fill all the fields!");
        }
        else {
            int minutes = 0;
            Sport preferredSport = null;
            if (min30.isSelected()) { minutes = 30; }
            if (min45.isSelected()) { minutes = 45; }
            if (min60.isSelected()) { minutes = 60; }
            if (min90.isSelected()) { minutes = 90; }
            if (sport.getValue().equals("Football")) { preferredSport = new Football(); }
            if (sport.getValue().equals("Basketball")) { preferredSport = new Basketball(); }
            if (sport.getValue().equals("Volleyball")) { preferredSport = new Volleyball(); }
            if (sport.getValue().equals("Tennis")) { preferredSport = new Tennis(); }
            Database.createMatch(matchName.getText(), preferredSport, place.toString(), date.getValue(), time.getText(), minutes);
        }
    }
    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }

    @Override
    public void preloadData(User volunteer) throws IOException {

    }

}
