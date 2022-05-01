package scenes;

import classes.Database;
import classes.ImageHandler;
import classes.Match;
import classes.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class matchPageController implements MatchController, MainController{
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
    private Button selectPosition0,selectPosition1,selectPosition2,selectPosition3,selectPosition4,selectPosition5,selectPosition6,selectPosition7,selectPosition8,selectPosition9,selectPosition10,selectPosition11;
    private User user;
    private Match match;
    private File fieldFile; // the image file of the field

    @FXML
    public void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }


    //When a user clicks on an available position, an alert pops up in order for the user to confirm joining the game
    //When the user confirms, it adds the user to the match in the preferred position
    @FXML
    public void clickOnSelectPosition(ActionEvent event) throws IOException{
        Stage stage = (Stage) myPane.getScene().getWindow();
        Alert.AlertType type = Alert.AlertType.NONE;
        Alert alert = new Alert(type,"");

        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText("Are you sure you want to join the match in this position?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK)
        {
            Alert infoAlert = new Alert(Alert.AlertType.NONE);
            infoAlert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            infoAlert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
            infoAlert.initOwner(stage);
            infoAlert.getDialogPane().setContentText("You successfully joined the match!");
            infoAlert.show();

            int position = getPositionSelection(event);

            //Pozisyona göre eklemiyor
            try {
                Database.addMatch(user,match);
            }
            catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            SceneChanger sc = new SceneChanger();
            MatchController matchCont = new JoinedMatchPageController();
            MainController mainCont = new JoinedMatchPageController();

            //User oyuna katıldıktan sonra Joined_Match_Page'e geçilmesi lazım
            /*try {
                sc.changeScenes(e, "Joined_Match_Page.fxml", "Teamder", user, match, matchCont, mainCont);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }*/
        }
    }

    private int getPositionSelection(ActionEvent event)
    {
        Button clickedButton = (Button) event.getTarget();
        if(clickedButton.equals(selectPosition0)){return 0;}
        if(clickedButton.equals(selectPosition1)){return 1;}
        if(clickedButton.equals(selectPosition2)){return 2;}
        if(clickedButton.equals(selectPosition3)){return 3;}
        if(clickedButton.equals(selectPosition4)){return 4;}
        if(clickedButton.equals(selectPosition5)){return 5;}
        if(clickedButton.equals(selectPosition6)){return 6;}
        if(clickedButton.equals(selectPosition7)){return 7;}
        if(clickedButton.equals(selectPosition8)){return 8;}
        if(clickedButton.equals(selectPosition9)){return 9;}
        if(clickedButton.equals(selectPosition10)){return 10;}
        if(clickedButton.equals(selectPosition11)){return 11;}
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
        this.user = user;
    }
}
