package scenes;

import classes.Database;
import classes.ImageHandler;
import classes.Match;
import classes.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private User currentUser;
    private User[] users;
    private Match match;

    /**
     * This method is called when the user clicks on the Close button.
     * It changes the scene to the previous page.
     * @param event the event that triggers the method
     */
    @FXML
    public void clickOnCancel(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        MainController controllerClass = new profileController();
        sc.changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), controllerClass);
    }

    /**
     * This method is called when the user clicks on a position button
     * If the user is not in the match and the selected position is not occupied, confirmWindow emerges
     * If the user is in the match and the selected position is not occupied, informationWindow emerges
     * If the selected position is occupied, profileWindow of the player in that position emerges
     * @param event the event that triggers the method
     */
    @FXML
    public void clickOnPosition(ActionEvent event) throws IOException, SQLException {
        if(Database.isUserInMatch(currentUser.getUserName(), match.getName()))
        //pozisyonun doluluğuna bakmıyor
        {
            profileWindow(event);
        }
        else //if(user is not in match && the position is not occupied)
        {
            confirmWindow(event);
        }
        /*else if(position is occupied)
        {
            profileWindow(event);
        }
        else if(Database.isUserInMatch(currentUser.getUserName(), match.getName()) && the position is not occupied)
        {
            errorWindow(event);
        }*/
    }

    /**
     * This method creates a window for user to confirm that they want to join the match
     * If they confirm, it adds the user to match
     * @param event the event that triggers the method
     */
    private void confirmWindow(ActionEvent event)
    {
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
                Database.addMatch(currentUser,match);
            }
            catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * This method creates a window that contains the rate and the link to the profile of the player in selected position
     * @param event the event that triggers the method
     */
    private void profileWindow(ActionEvent event)
    {
        int playerPosition = getPositionSelection(event);

        Stage stage = new Stage();

        Label labelRate = new Label();
        labelRate.setText("Rate: " ); // + users[playerPosition].getRating() users pozisyona göre database den alınamıyor

        Hyperlink profile = new Hyperlink();
        profile.setText("userName"); // + users[playerPosition].getName() users pozisyona göre database den alınamıyor

        SceneChanger sc = new SceneChanger();

        //eğer kendi profili ise Profile_Page
        //if(users[playerPosition].equals(currentUser))  daha users pozisyona göre database den alınamıyor
        profile.setOnAction(e -> {
            try {
                stage.close();
                MatchController matchController = new profileWithCloseButtonController();
                MainController mainController = new profileWithCloseButtonController();
                sc.changeScenes(event, "Profile_With_CloseButton.fxml", "Teamder", SceneChanger.getLoggedInUser(),match,matchController, mainController);
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

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelRate,profile);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * This method creates a window that informs the user that they have already joined the match and can not join again
     * @param event the event that triggers the method
     */
    private void errorWindow(ActionEvent event)
    {
        Stage stage = new Stage();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.initOwner(stage);
        alert.getDialogPane().setContentText("You have already joined the match!");
        alert.show();
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
            // the image file of the field
            File fieldFile = new File(ImageHandler.IMAGE_PATH + "football_field.jpeg");
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
        this.currentUser = user;
    }
}
