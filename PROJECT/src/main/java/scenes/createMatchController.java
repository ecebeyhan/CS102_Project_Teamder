package scenes;

import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class createMatchController {

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
    protected void clickOnCreate(ActionEvent event) {

    }
    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
//        sc.changeScenes(event,"Profile_Page.fxml", "Teamder | Profile Page", User user, loginController)
    }


    public void datePick(ActionEvent event) {

    }
}
