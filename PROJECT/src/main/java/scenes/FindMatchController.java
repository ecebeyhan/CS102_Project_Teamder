package scenes;

import classes.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class FindMatchController implements MainController, Initializable {

    @FXML
    private ComboBox<String> cityComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label matchFoundLabel;
    @FXML
    private RadioButton football;
    @FXML
    private RadioButton basketball;
    @FXML
    private RadioButton tennis;
    @FXML
    private RadioButton volleyball;
    @FXML
    private TextField matchNameTField;


    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException, SQLException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }

    @FXML
    void clickSearchButton(ActionEvent event) {

    }

    @Override
    public void preloadData(User volunteer) throws IOException {

    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cityComboBox.setItems(FXCollections.observableArrayList("Trabzon", "Ankara", "Istanbul", "Izmir", "Izmit"));
    }


}
