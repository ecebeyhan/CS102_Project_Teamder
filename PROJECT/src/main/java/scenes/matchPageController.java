package scenes;

import classes.Match;
import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.io.IOException;
import java.util.ArrayList;

public class matchPageController implements MatchController, MainController{

    @FXML
    private Text placeText;
    @FXML
    private Text timeText;
    @FXML
    private Text dateText;
    @FXML
    private Text matchNameText;
    @FXML
    private Button selectPosition1,selectPosition2,selectPosition3,selectPosition4,selectPosition5,selectPosition6,selectPosition7,selectPosition8,selectPosition9,selectPosition10,selectPosition11,selectPosition12;

    public void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }


    //When a user clicks on an available position, a window pops up in order for the user to join the game
    //When the user confirms, it adds the user to the match in the preferred position
    public void clickOnSelectPosition(ActionEvent event) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label();
        label.setText("Join the match in this position");

        Button joinMatch = new Button();
        joinMatch.setText("JOIN");

        //joinMatch butonuna tıklanılınca user ı pozisyona göre maça eklemesi gerekiyor
        //joinMatch.setOnAction(e -> );

        VBox layout = new VBox(10);
        layout.getChildren().addAll(joinMatch);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    @Override
    public void preLoadMatch(Match match) throws IOException {
        placeText.setText(match.getPlace());
        timeText.setText(match.getStartTime().toString());
        dateText.setText(match.getDate().toString());
        matchNameText.setText(match.getName());
    }

    @Override
    public void preloadData(User user) throws IOException {

    }
}
