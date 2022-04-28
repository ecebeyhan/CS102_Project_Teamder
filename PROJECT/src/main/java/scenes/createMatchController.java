package scenes;

import classes.User;
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
    private Label uniLabel;


    @FXML
    protected void clickOnCreate(ActionEvent event) {

    }
    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
//        sc.changeScenes(event,"Profile_Page.fxml", "Teamder | Profile Page");
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", uniLabel.getText(), controllerClass);
    }


    public void datePick(ActionEvent event) {

    }

    @Override
    public void preloadData(User volunteer) throws IOException {

    }

    public void setLabel(String name) {
        uniLabel.setText(name);
    }
}
