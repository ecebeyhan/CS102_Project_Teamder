package scenes;

import classes.Match;

import java.io.IOException;

public interface MatchController {
    public abstract void preLoadMatch(Match match) throws IOException;
}
