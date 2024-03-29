package scenes;

import classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class createMatchController implements  MainController, Initializable {

    @FXML
    private ComboBox<String> place;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> sport;
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
    private Label errorLabel;
    
    @FXML
    protected void clickOnCreate(ActionEvent event) throws SQLException, IOException {

        if(errorLabel.getText() != null){
            errorLabel.setText(null);
        }
        if (matchName.getText().isEmpty() || time.getText().isEmpty() || date.getValue() == null || sport.getValue() == null || place.getValue() == null) {
            errorLabel.setText("Please fill all the fields!");
        }
        else {
            try{
                for (int i = 0; i < matchName.getText().length(); i++) {
                    if (matchName.getText().charAt(i) == '\'') {
                        errorLabel.setText("Please don't use ' in the name!"); return;
                    }

                 }
                LocalDate d = date.getValue();
                LocalTime t = LocalTime.parse(time.getText());
                LocalDateTime now = LocalDateTime.now();
                if ((now.toLocalDate().isAfter(d))|| (now.toLocalDate().equals(d) && now.toLocalTime().isAfter(t) ) ) {
                    errorLabel.setText("Please select a valid date and time");
                    return;
                }
                if( Database.doesMatchNameExist(matchName.getText())){
                    errorLabel.setText("There is already a match whose name is " + matchName.getText() + ". Please enter another name.");
                    return;
                }
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
                if( !Database.canUserJoinMatch(new Match(matchName.getText(), preferredSport, (String) place.getValue(), date.getValue(), LocalTime.parse(time.getText()), minutes))){
                    errorLabel.setText("You already have match at that time!");
                    return;
                }
                Database.createMatch(event, matchName.getText(), preferredSport, (String) place.getValue(), date.getValue(), LocalTime.parse(time.getText()), minutes);
            }
            catch (DateTimeParseException e){
                errorLabel.setText("Please enter the time in \"HH:mm \" format");
            }

        }
    }

    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException, SQLException {
        new SceneChanger().changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
    }

    @Override
    public void preloadData(User user) throws IOException {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sport.setItems(FXCollections.observableArrayList("Football", "Basketball", "Tennis", "Volleyball"));
        place.setItems(FXCollections.observableArrayList("Trabzon", "Ankara", "Istanbul", "Izmir", "Izmit"));
    }
}
