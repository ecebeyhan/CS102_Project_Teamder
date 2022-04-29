package scenes;

import classes.Database;
import classes.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

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
    void clickSearchButton(ActionEvent event) throws SQLException {
        if (!football.isSelected() && !basketball.isSelected() && !tennis.isSelected() && !volleyball.isSelected()) {
            matchFoundLabel.setText("Please select a sport"); return;
        }
        if (cityComboBox.getValue() == null) {
            matchFoundLabel.setText("Please select a city"); return;
        }
        if (datePicker.getValue() == null) {
            matchFoundLabel.setText("Please select a date"); return;
        }
        String sportPreffered = "";
        if (football.isSelected()) { sportPreffered = "Football"; }
        else if (basketball.isSelected()) { sportPreffered = "Basketball"; }
        else if (tennis.isSelected()) { sportPreffered = "Tennis"; }
        else if (volleyball.isSelected()) { sportPreffered = "Volleyball"; }
        String city = (String) cityComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String matchName = matchNameTField.getText();
        Database.filterMatches(sportPreffered, city, date, matchName, matchFoundLabel); // arraylist of matches
    }

    @Override
    public void preloadData(User volunteer) throws IOException {

    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cityComboBox.setItems(FXCollections.observableArrayList("Trabzon", "Ankara", "Istanbul", "Izmir", "Izmit"));
    }


}
