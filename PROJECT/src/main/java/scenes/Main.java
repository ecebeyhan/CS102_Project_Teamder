package scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    /**
     * The start method is called when the application is launched.
     * It loads the Welcome Page and starts the application.
     * @param stage the stage of the application
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Welcome_To_Teamder.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);
        stage.setTitle("Teamder | Welcome Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}