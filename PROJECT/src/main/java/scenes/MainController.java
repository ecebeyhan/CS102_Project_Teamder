package scenes;

import classes.User;

import java.io.IOException;

public interface MainController {
    public abstract void preloadData(User user) throws IOException;
}
