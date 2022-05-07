package scenes;

import classes.Match;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import classes.User;

import java.io.IOException;


public class SceneChanger {
    
    public static User loggedInUser;
    protected Task<Void> newScene;

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

    /**
     * This method will change scenes and preload the next scene with specified User object
     * @param event ActionEvent that triggered the change
     * @param viewName .fxml file name for the view (for example "Log_In.fxml")
     * @param title Title of the new scene
     * @param user User object that will be preloaded in the next scene
     * @param controller Class of the controller for the next scene
     */
    public void changeScenes(ActionEvent event, String viewName, String title, User user, MainController controller) throws IOException
    {
        final MainController[] controllerClass = {controller};
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        //Set the loading page first
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Loading_Page.fxml"));
        Scene primaryScene = new Scene(fxmlLoader.load(), 640, 400);
        stage.setScene(primaryScene);
        stage.show();

        //set up the scene on a separate task
        newScene = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(viewName));
                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Scene scene = new Scene(parent);
                //access the controller class and preloaded the User data
                controllerClass[0] = loader.getController();
                try {
                    controllerClass[0].preloadData(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.hide();
                        stage.setTitle(title);
                        stage.setScene(scene);
                        stage.show();
                        System.out.println("finished scene");
                    }
                });
                return null;
            }
        };
        //start the task
        Thread th = new Thread(newScene);
        th.setDaemon(true);
        th.start();
    }

    /**
     * This method will change scenes and preload the next scene with specified Match & User objects
     * @param viewName .fxml file name for the view (for example "Log_In.fxml")
     * @param title Title of the new scene
     * @param user User object that will be preloaded in the next scene
     * @param match Match object that will be preloaded in the next scene
     * @param matchControllerClass Class of the controller for the next scene
     * @param controllerClass Class of the controller for the next scene
     */
    public void changeScenes(ActionEvent event, String viewName, String title, User user, Match match, MatchController matchControllerClass, MainController controllerClass) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        //access the controller class and preloaded the User data
        controllerClass = loader.getController();
        controllerClass.preloadData(user);

        //access the controller class and preloaded the Match data
        matchControllerClass = loader.getController();
        matchControllerClass.preLoadMatch(match);

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
