package scenes;

import classes.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public interface MainController {
    public abstract void preloadData(User volunteer) throws IOException;
}
