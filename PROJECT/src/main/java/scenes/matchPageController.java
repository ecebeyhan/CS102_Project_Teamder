package scenes;

import classes.Database;
import classes.ImageHandler;
import classes.Match;
import classes.User;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class matchPageController implements MatchController, MainController{
    @FXML
    public ImageView footballField;
    @FXML
    public ImageView volleyballField;
    @FXML
    public ImageView tennisField;
    @FXML
    public ImageView basketballField;
    @FXML
    public Text placeText;
    @FXML
    public Text timeText;
    @FXML
    public Text dateText;
    @FXML
    public Text matchNameText;
    @FXML
    public Pane myPane;
    @FXML
    public Button footballPosition0,footballPosition1,footballPosition2,footballPosition3,footballPosition4,footballPosition5,footballPosition6,footballPosition7,footballPosition8,footballPosition9,footballPosition10,footballPosition11;
    @FXML
    public Button volleyballPosition0,volleyballPosition1,volleyballPosition2,volleyballPosition3,volleyballPosition4,volleyballPosition5,volleyballPosition6,volleyballPosition7;
    @FXML
    public Button tennisPosition0,tennisPosition1,tennisPosition2,tennisPosition3;
    @FXML
    public Button basketballPosition0,basketballPosition1,basketballPosition2,basketballPosition3,basketballPosition4,basketballPosition5,basketballPosition6,basketballPosition7,basketballPosition8,basketballPosition9;
    @FXML
    public Button quitButton;
    @FXML
    public TextField sender;
    @FXML
    public TextArea receiver;
    private User currentUser;
    private Match match;

    /**
     * This method is called when the user clicks on the Close button.
     * It changes the scene to the previous page.
     * @param event the event that triggers the method
     */
    @FXML
    public void clickOnCancel(ActionEvent event) throws IOException {
        new SceneChanger().changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
    }

    @FXML
    public void clickOnSend(ActionEvent event) {
        Database.sendMessage(sender.getText(), match, receiver);
        sender.clear();
    }

    @FXML
    public void clickOnUpdate(ActionEvent event) {
        Database.getMessage(match.getName(), receiver);
    }

    /**
     * This method creates a window for user to confirm that they want to quit the match
     * If they confirm, it removes the user from the match
     * @param event the event that triggers the method
     */
    @FXML
    public void clickOnQuit(ActionEvent event) throws IOException{
        Stage stage = (Stage) myPane.getScene().getWindow();
        Alert.AlertType type = Alert.AlertType.NONE;
        Alert alert = new Alert(type,"");

        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText("Are you sure you want to quit the match?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK)
        {
            Alert infoAlert = new Alert(Alert.AlertType.NONE);
            infoAlert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            infoAlert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
            infoAlert.initOwner(stage);

            Database.removePlayerFromMatch(currentUser,match);
            infoAlert.getDialogPane().setContentText("You quit the match!");
            infoAlert.show();
            new SceneChanger().changeScenes(event, "Profile_Page.fxml", "Teamder", SceneChanger.getLoggedInUser(), new profileController());
        }
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
        int position = getPositionSelection(event);

        if(Database.getUserInPosition(match.getName(),position) != null)
        {
            profileWindow(event);
        }
        else if(!Database.isUserInMatch(currentUser.getUserName(), match.getName()))
        {
            confirmWindow(event);
        }
        else
        {
            errorWindow(event);
        }
    }

    /**
     * This method creates a window for user to confirm that they want to join the match
     * If they confirm, it adds the user to match
     * @param event the event that triggers the method
     */
    private void confirmWindow(ActionEvent event)
    {
        Button clickedButton = (Button) event.getTarget();

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

            int position = getPositionSelection(event);

            try {
                Database.addMatch(currentUser,match,position);
                quitButton.setVisible(true);
                infoAlert.getDialogPane().setContentText("You successfully joined the match!");
                infoAlert.show();
                clickedButton.setStyle("-fx-background-color: #000000; ");
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
    private void profileWindow(ActionEvent event) throws SQLException {
        int position = getPositionSelection(event);
        User selectedUser = Database.getUserInPosition(match.getName(),position);

        Stage stage = new Stage();

        Label labelRate = new Label();
        labelRate.setText("Rate: " + selectedUser.getRating());

        Hyperlink profile = new Hyperlink();
        profile.setText(selectedUser.getName());

        //if the selected position is the user's own position, the user is directed to their own profile
        if(selectedUser.equals(currentUser))
        profile.setOnAction(e -> {
            try {
                stage.close();
                MatchController matchController = new profileWithCloseButtonController();
                MainController mainController = new profileWithCloseButtonController();
                new SceneChanger().changeScenes(event, "Profile_With_CloseButton.fxml", "Teamder", SceneChanger.getLoggedInUser(),match,matchController, mainController);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        else if(!selectedUser.equals(currentUser)){
            profile.setOnAction(e -> {
                try {
                    stage.close();
                    MatchController matchController = new profileWithCloseButtonController();
                    MainController mainController = new profileWithCloseButtonController();
                    new SceneChanger().changeScenes(event, "Friend_Page.fxml", "Teamder", selectedUser,match,matchController, mainController);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

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
        Stage stage = (Stage) myPane.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
        alert.initOwner(stage);
        alert.getDialogPane().setContentText("You have already joined the match!");
        alert.show();
    }

    /**
     * This method returns the position the user selected according to the button they clicked
     * @param event the event that triggers the method
     * @return the position the user selected
     */
    private int getPositionSelection(ActionEvent event)
    {
        Button clickedButton = (Button) event.getTarget();
        if(clickedButton.equals(footballPosition0) || clickedButton.equals(basketballPosition0) || clickedButton.equals(volleyballPosition0) || clickedButton.equals(tennisPosition0)){return 0;}
        if(clickedButton.equals(footballPosition1) || clickedButton.equals(basketballPosition1) || clickedButton.equals(volleyballPosition1) || clickedButton.equals(tennisPosition1)){return 1;}
        if(clickedButton.equals(footballPosition2) || clickedButton.equals(basketballPosition2) || clickedButton.equals(volleyballPosition2) || clickedButton.equals(tennisPosition2)){return 2;}
        if(clickedButton.equals(footballPosition3) || clickedButton.equals(basketballPosition3) || clickedButton.equals(volleyballPosition3) || clickedButton.equals(tennisPosition3)){return 3;}
        if(clickedButton.equals(footballPosition4) || clickedButton.equals(basketballPosition4) || clickedButton.equals(volleyballPosition4)){return 4;}
        if(clickedButton.equals(footballPosition5) || clickedButton.equals(basketballPosition5) || clickedButton.equals(volleyballPosition5)){return 5;}
        if(clickedButton.equals(footballPosition6) || clickedButton.equals(basketballPosition6) || clickedButton.equals(volleyballPosition6)){return 6;}
        if(clickedButton.equals(footballPosition7) || clickedButton.equals(basketballPosition7) || clickedButton.equals(volleyballPosition7)){return 7;}
        if(clickedButton.equals(footballPosition8) || clickedButton.equals(basketballPosition8)){return 8;}
        if(clickedButton.equals(footballPosition9) || clickedButton.equals(basketballPosition9)){return 9;}
        if(clickedButton.equals(footballPosition10)){return 10;}
        if(clickedButton.equals(footballPosition11)){return 11;}
        return -1; //invalid position
    }

    /**
     * This method arranges the match field according to the sport of the match
     * @param sportName the sport of the match
     */
    private void arrangeMatchField(String sportName)
    {
        if(sportName.equals("Football"))
        {
            setVolleyballFieldInvisible();
            setTennisFieldInvisible();
            setBasketballFieldInvisible();
            try {
                setPositionButtonsFootball();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
        else if(sportName.equals("Volleyball"))
        {
            setFootballFieldInvisible();
            setTennisFieldInvisible();
            setBasketballFieldInvisible();
            try {
                setPositionButtonsVolleyball();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
                // the image file of the field
                File fieldFile = new File(ImageHandler.IMAGE_PATH + "volleyball_field.jpeg");
                BufferedImage bufferedImage = ImageIO.read(fieldFile);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                volleyballField.setImage(img);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        else if(sportName.equals("Tennis"))
        {
            setFootballFieldInvisible();
            setVolleyballFieldInvisible();
            setBasketballFieldInvisible();
            try {
                setPositionButtonsTennis();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
                // the image file of the field
                File fieldFile = new File(ImageHandler.IMAGE_PATH + "tennis_field.jpeg");
                BufferedImage bufferedImage = ImageIO.read(fieldFile);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                tennisField.setImage(img);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        else if(sportName.equals("Basketball"))
        {
            setFootballFieldInvisible();
            setVolleyballFieldInvisible();
            setTennisFieldInvisible();
            try {
                setPositionButtonsBasketball();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
                // the image file of the field
                File fieldFile = new File(ImageHandler.IMAGE_PATH + "basketball_field.jpeg");
                BufferedImage bufferedImage = ImageIO.read(fieldFile);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                basketballField.setImage(img);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        else{
            setBasketballFieldInvisible();
            setTennisFieldInvisible();
            setVolleyballFieldInvisible();
            setFootballFieldInvisible();
        }
    }

    /**
     * This method sets the football field invisible when the match is not a football match
     */
    private void setFootballFieldInvisible()
    {
        footballPosition0.setVisible(false);
        footballPosition1.setVisible(false);
        footballPosition2.setVisible(false);
        footballPosition3.setVisible(false);
        footballPosition4.setVisible(false);
        footballPosition5.setVisible(false);
        footballPosition6.setVisible(false);
        footballPosition7.setVisible(false);
        footballPosition8.setVisible(false);
        footballPosition9.setVisible(false);
        footballPosition10.setVisible(false);
        footballPosition11.setVisible(false);
        footballField.setVisible(false);
    }

    /**
     * This method sets the volleyball field invisible when the match is not a volleyball match
     */
    private void setVolleyballFieldInvisible()
    {
        volleyballPosition0.setVisible(false);
        volleyballPosition1.setVisible(false);
        volleyballPosition2.setVisible(false);
        volleyballPosition3.setVisible(false);
        volleyballPosition4.setVisible(false);
        volleyballPosition5.setVisible(false);
        volleyballPosition6.setVisible(false);
        volleyballPosition7.setVisible(false);
        volleyballField.setVisible(false);
    }

    /**
     * This method sets the tennis field invisible when the match is not a tennis match
     */
    private void setTennisFieldInvisible()
    {
        tennisPosition0.setVisible(false);
        tennisPosition1.setVisible(false);
        tennisPosition2.setVisible(false);
        tennisPosition3.setVisible(false);
        tennisField.setVisible(false);
    }

    /**
     * This method sets the basketball field invisible when the match is not a basketball match
     */
    private void setBasketballFieldInvisible()
    {
        basketballPosition0.setVisible(false);
        basketballPosition1.setVisible(false);
        basketballPosition2.setVisible(false);
        basketballPosition3.setVisible(false);
        basketballPosition4.setVisible(false);
        basketballPosition5.setVisible(false);
        basketballPosition6.setVisible(false);
        basketballPosition7.setVisible(false);
        basketballPosition8.setVisible(false);
        basketballPosition9.setVisible(false);
        basketballField.setVisible(false);
    }

    /**
     * This method sets buttons of the football field's background color black to signify that position is occupied
     */
    private void setPositionButtonsFootball() throws SQLException
    {
        if(Database.getUserInPosition(match.getName(), 0) != null)
        {
            footballPosition0.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 1) != null)
        {
            footballPosition1.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 2) != null)
        {
            footballPosition2.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 3) != null)
        {
            footballPosition3.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 4) != null)
        {
            footballPosition4.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 5) != null)
        {
            footballPosition5.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 6) != null)
        {
            footballPosition6.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 7) != null)
        {
            footballPosition7.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 8) != null)
        {
            footballPosition8.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 9) != null)
        {
            footballPosition9.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 10) != null)
        {
            footballPosition10.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 11) != null)
        {
            footballPosition11.setStyle("-fx-background-color: #000000; ");
        }
    }
    /**
     * This method sets buttons of the basketball field's background color black to signify that position is occupied
     */
    private void setPositionButtonsBasketball() throws SQLException
    {
        if(Database.getUserInPosition(match.getName(), 0) != null)
        {
            basketballPosition0.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 1) != null)
        {
            basketballPosition1.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 2) != null)
        {
            basketballPosition2.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 3) != null)
        {
            basketballPosition3.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 4) != null)
        {
            basketballPosition4.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 5) != null)
        {
            basketballPosition5.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 6) != null)
        {
            basketballPosition6.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 7) != null)
        {
            basketballPosition7.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 8) != null)
        {
            basketballPosition8.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 9) != null)
        {
            basketballPosition9.setStyle("-fx-background-color: #000000; ");
        }
    }
    /**
     * This method sets buttons of the volleyball field's background color black to signify that position is occupied
     */
    private void setPositionButtonsVolleyball() throws SQLException
    {
        if(Database.getUserInPosition(match.getName(), 0) != null)
        {
            volleyballPosition0.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 1) != null)
        {
            volleyballPosition1.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 2) != null)
        {
            volleyballPosition2.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 3) != null)
        {
            volleyballPosition3.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 4) != null)
        {
            volleyballPosition4.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 5) != null)
        {
            volleyballPosition5.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 6) != null)
        {
            volleyballPosition6.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 7) != null)
        {
            volleyballPosition7.setStyle("-fx-background-color: #000000; ");
        }
    }
    /**
     * This method sets buttons of the tennis field's background color black to signify that position is occupied
     */
    private void setPositionButtonsTennis() throws SQLException
    {
        if(Database.getUserInPosition(match.getName(), 0) != null)
        {
            tennisPosition0.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 1) != null)
        {
            tennisPosition1.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 2) != null)
        {
            tennisPosition2.setStyle("-fx-background-color: #000000; ");
        }
        if(Database.getUserInPosition(match.getName(), 3) != null)
        {
            tennisPosition3.setStyle("-fx-background-color: #000000; ");
        }
    }


    @Override
    public void preLoadMatch(Match match) {
        placeText.setText(match.getPlace());
        timeText.setText(match.getStartTime().toString());
        dateText.setText(match.getDate().toString());
        matchNameText.setText(match.getName());
        this.match = match;
        arrangeMatchField(match.getSport().getName());
        try {
            if(!Database.isUserInMatch(currentUser.getUserName(), match.getName()))
            {
                quitButton.setVisible(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void preloadData(User user) throws IOException {
        this.currentUser = user;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Database.getMessage(match.getName(), receiver);
                });
            }
        }, 250, 1500);
    }
}
