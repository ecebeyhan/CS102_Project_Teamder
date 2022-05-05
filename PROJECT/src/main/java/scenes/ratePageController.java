package scenes;
import classes.Database;
import classes.Match;
import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ratePageController implements MainController, MatchController {
    private  Match match;
    @FXML
    private ComboBox comboBox1;
    @FXML
    private ComboBox comboBox2;
    @FXML
    private ComboBox comboBox3;
    @FXML
    private ComboBox comboBox4;
    @FXML
    private ComboBox comboBox5;
    @FXML
    private ComboBox comboBox6;
    @FXML
    private ComboBox comboBox7;
    @FXML
    private ComboBox comboBox8;
    @FXML
    private ComboBox comboBox9;
    @FXML
    private ComboBox comboBox10;
    @FXML
    private ComboBox comboBox11;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;
    @FXML
    private Label label7;
    @FXML
    private Label label8;
    @FXML
    private Label label9;
    @FXML
    private Label label10;
    @FXML
    private Label label11;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private ArrayList<ComboBox> comboBoxes = new ArrayList<>();

    @Override
    public void preloadData(User user) throws IOException {

    }

    @Override
    public void preLoadMatch(Match match) throws IOException {
        this.match = match;
        try {
            playerNames = Database.getPlayersFromMatch(match);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < playerNames.size(); i++){
            if(playerNames.get(i).equals(SceneChanger.loggedInUser.getUserName()) ){
                playerNames.remove(i);
            }
        }

        labels = new ArrayList<>();
        labels.add(label1);
        labels.add(label2);
        labels.add(label3);
        labels.add(label4);
        labels.add(label5);
        labels.add(label6);
        labels.add(label7);
        labels.add(label8);
        labels.add(label9);
        labels.add(label10);
        labels.add(label11);

        comboBoxes = new ArrayList<>();
        comboBoxes.add(comboBox1);
        comboBoxes.add(comboBox2);
        comboBoxes.add(comboBox3);
        comboBoxes.add(comboBox4);
        comboBoxes.add(comboBox5);
        comboBoxes.add(comboBox6);
        comboBoxes.add(comboBox7);
        comboBoxes.add(comboBox8);
        comboBoxes.add(comboBox9);
        comboBoxes.add(comboBox10);
        comboBoxes.add(comboBox11);

        for(int i = 0; i < playerNames.size(); i ++){
            labels.get(i).setVisible(true);
            comboBoxes.get(i).setVisible(true);
            labels.get(i).setText(playerNames.get(i));
            comboBoxes.get(i).getItems().addAll("1" , "2" , "3" , "4" , "5");
        }
        for(int i = playerNames.size(); i < 11; i ++){
            labels.get(i).setVisible(false);
            comboBoxes.get(i).setVisible(false);
        }

    }
    public void clickOnRate(ActionEvent event) throws IOException, SQLException {
        for(int i = 0; i < playerNames.size(); i++){
            Database.updateRatings(Integer.valueOf((String) comboBoxes.get(i).getValue()), playerNames.get(i));
        }
        Database.setRateInactive(SceneChanger.loggedInUser.getName(), match.getName());

        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder | Profile Page", SceneChanger.loggedInUser, new profileController());
    }

    public void clickOnBack(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
    }
}
