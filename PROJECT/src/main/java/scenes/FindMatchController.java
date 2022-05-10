package scenes;

import classes.Database;
import classes.Match;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private TableView<Match> matchTable;
    @FXML
    private TableColumn<Match, Hyperlink> matchNameColumn;
    @FXML
    private TableColumn<Match, LocalDate> matchDateColumn;
    @FXML
    private TableColumn<Match, String> matchCityColumn;
    @FXML
    private Label errorLabel;

    @FXML
    protected void clickOnCancel(ActionEvent event) throws IOException, SQLException {
        new SceneChanger().changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
    }

    @FXML
    void clickSearchButton(ActionEvent event) throws SQLException {
        matchFoundLabel.setText("Found 0 match(es)");
        ObservableList<Match> anyMatches = matchTable.getItems(); //Gets matches from Tableview object if there are any.
        String city = "";
        LocalDate date = null;
        if( anyMatches.size() > 0 ){
            matchTable.getItems().clear();
        }
        if( errorLabel.getText() != null){
            errorLabel.setText(null);
        }
        if (!football.isSelected() && !basketball.isSelected() && !tennis.isSelected() && !volleyball.isSelected()) {
            matchFoundLabel.setText("Please select a sport");
            return;
        }
        if (cityComboBox.getValue() != null) {
            city = (String) cityComboBox.getValue();
        }
        if (datePicker.getValue() != null) {
            date = datePicker.getValue();
        }
        String sportPreffered = "";
        if (football.isSelected()) {
            sportPreffered = "Football";
        } else if (basketball.isSelected()) {
            sportPreffered = "Basketball";
        } else if (tennis.isSelected()) {
            sportPreffered = "Tennis";
        } else if (volleyball.isSelected()) {
            sportPreffered = "Volleyball";
        }
        String matchName = matchNameTField.getText();
        LocalDateTime now = LocalDateTime.now();
        ObservableList<Match> matches = null;
        if (date == null) {
            matches = Database.alternativeFilter(sportPreffered, city, matchName, matchFoundLabel);
        }
        else if( now.toLocalDate().isAfter(date)){
            errorLabel.setText("Please select a valid date");
            return;
        }
        else if( now.toLocalDate().equals(date)){
            try {
                matches = Database.filterTodaysMatches(sportPreffered, city, date, matchName, matchFoundLabel);
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }
        else{
            try {
                matches = Database.filterMatches(sportPreffered, city, date, matchName, matchFoundLabel);
            } catch (SQLException e) {
                e.getStackTrace();
            }
        }
        assert matches != null;
        matchTable.getItems().addAll(matches);
        createLinks();
    }

    @Override
    public void preloadData(User volunteer) throws IOException {

    }

    /**
     * Creates hyperlinks for each match in the table.
     */
    private void createLinks(){
        ObservableList<Hyperlink> links = FXCollections.observableArrayList();
        for( int i = 0; i < matchTable.getItems().size(); i++){
            Match m = matchTable.getItems().get(i);
            TableColumn<Match, ?> col = matchTable.getColumns().get(0);
            Hyperlink link = (Hyperlink) col.getCellObservableValue(m).getValue();
            links.add(link);
        }
        for (Hyperlink link : links) {
            link.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    try {
                        Match match = Database.getMatch(((Hyperlink) t.getSource()).getText());
                        MainController MatchPage = new matchPageController();
                        MatchController userPage = new matchPageController();
                        new SceneChanger().changeScenes(t, "Match_Page.fxml", "Teamder | Match Page", SceneChanger.getLoggedInUser(), match, userPage, MatchPage);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        cityComboBox.setItems(FXCollections.observableArrayList("Trabzon", "Ankara", "Istanbul", "Izmir", "Izmit"));
        matchNameColumn.setCellValueFactory( new PropertyValueFactory<Match, Hyperlink>("matchLink"));
        matchDateColumn.setCellValueFactory( new PropertyValueFactory<Match, LocalDate>("date"));
        matchCityColumn.setCellValueFactory( new PropertyValueFactory<Match, String>("place"));
        matchFoundLabel.setText("Found 0 match(es)");
    }


}
