package scenes;

import classes.Database;
import classes.ImageHandler;
import classes.Match;
import classes.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class JoinedMatchPageController implements MatchController, MainController{
    @FXML
    public ImageView footballField;
    @FXML
    private Text placeText;
    @FXML
    private Text timeText;
    @FXML
    private Text dateText;
    @FXML
    private Text matchNameText;
    @FXML
    private Pane myPane;
    @FXML
    private Button player0,player1,player2,player3,player4,player5,player6,player7,player8,player9,player10,player11;
    private User[] users;
    private User currentUser;
    private Match match;
    private File fieldFile; // the image file of the field

    public JoinedMatchPageController() {

    }
    @FXML
    public void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }


    //When a user clicks on an available position, an alert pops up in order for the user to confirm joining the game
    //When the user confirms, it adds the user to the match in the preferred position
    @FXML
    public void clickOnSelectPlayer(ActionEvent event) throws IOException, SQLException {
        int playerPosition = getPlayerSelection(event);

        Stage stage = new Stage();

        Label labelRate = new Label();
        labelRate.setText("Rate: " ); // + users[playerPosition].getRating() users pozisyona göre database den alınamıyor

        Hyperlink profile = new Hyperlink();
        profile.setText("userName"); // + users[playerPosition].getName() users pozisyona göre database den alınamıyor

        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();

        //eğer kendi profili ise Profile_Page
        //if(users[playerPosition].equals(currentUser))  daha users pozisyona göre database den alınamıyor
        profile.setOnAction(e -> {
            try {
                stage.close();
                sc.changeScenes(event, "Profile_Page.fxml", "Teamder", currentUser, controllerClass);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //
        /*else if(!users[playerPosition].equals(currentUser)){
        profile.setOnAction(e -> {
                    try {
                        sc.changeScenes(e, "Friend_Page.fxml", "Teamder", users[playerPosition], controllerClass);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        }*/

        //eğer kendi profili ise Profile_Page

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelRate,profile);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void initializePlayers()
    {
        //will initialize User[] players according to their positions
    }
    private int getPlayerSelection(ActionEvent event)
    {
        Button clickedButton = (Button) event.getTarget();
        if(clickedButton.equals(player0)){return 0;}
        if(clickedButton.equals(player1)){return 1;}
        if(clickedButton.equals(player2)){return 2;}
        if(clickedButton.equals(player3)){return 3;}
        if(clickedButton.equals(player4)){return 4;}
        if(clickedButton.equals(player5)){return 5;}
        if(clickedButton.equals(player6)){return 6;}
        if(clickedButton.equals(player7)){return 7;}
        if(clickedButton.equals(player8)){return 8;}
        if(clickedButton.equals(player9)){return 9;}
        if(clickedButton.equals(player10)){return 10;}
        if(clickedButton.equals(player11)){return 11;}
        return -1; //invalid position
    }

    @Override
    public void preLoadMatch(Match match) throws IOException {
        placeText.setText(match.getPlace());
        timeText.setText(match.getStartTime().toString());
        dateText.setText(match.getDate().toString());
        matchNameText.setText(match.getName());
        this.match = match;

        try{
            fieldFile = new File(ImageHandler.IMAGE_PATH + "football_field.jpeg");
            BufferedImage bufferedImage = ImageIO.read(fieldFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            footballField.setImage(img);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void preloadData(User user) throws IOException {
        currentUser = user;
    }
}
