package scenes;

import classes.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import classes.User;

import java.io.IOException;
import java.sql.SQLException;


public class SceneChanger {
    
    private static User loggedInUser;

    /**
     * This method will accept the title of the new scene, the .fxml file name for
     * the view and the ActionEvent that triggered the change
     * IMPORTANT: This method does not preload next scene with a User Object!!!
     * This method will be used only Welcome, Profile Creation and Login pages
     * since these ones does not require any user information to be preloaded
     * @param event ActionEvent that triggered the change
     * @param viewName .fxml file name for the view (for example "Log_In.fxml")
     * @param title Title of the new scene
     */
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void changeScenes(ActionEvent event, String viewName, String title, String name,MainController controllerClass) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        //access the controller class and preloaded the User data
        controllerClass = loader.getController();
        controllerClass.preloadData(Database.getUser(name));
        controllerClass.setLabel(name);
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will change scenes and preload the next scene with specified User object
     * @param event ActionEvent that triggered the change
     * @param viewName .fxml file name for the view (for example "Log_In.fxml")
     * @param title Title of the new scene
     * @param user User object that will be preloaded in the next scene
     * @param controllerClass Class of the controller for the next scene
     */
    public void changeScenes(ActionEvent event, String viewName, String title, User user, MainController controllerClass) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        //access the controller class and preloaded the User data
        controllerClass = loader.getController();
        controllerClass.preloadData(user);

        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }



    
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        SceneChanger.loggedInUser = loggedInUser;
    }
}
