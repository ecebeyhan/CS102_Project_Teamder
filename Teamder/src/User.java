import java.util.ArrayList;

public class User {

    private int ID;         // a uniqe ID for every users
    private String userName;
    private String password;
    private String sports;  // interested sports (gostermelik)
    private String bio;
    private MatchList matches;
    private ArrayList<User> friendsList;
    private Rate rating;

    public User(int ID, String name, String sports, String bio, MatchList matches, ArrayList<User> friendsList, Rate rating) {
        this.ID = ID;
        this.userName = name;
        this.sports = sports;
        this.bio = bio;
        this.matches = matches;
        this.friendsList = friendsList;
        this.rating = rating;
    }

    //-----------------------------------------------------------------
    //  Getter methods for all instance variables
    //-----------------------------------------------------------------
    public ArrayList<User> getFriendsList() {
        return friendsList;
    }

    public int getID() {
        return ID;
    }

    public MatchList getMatches() {
        return matches;
    }

    public Rate getRating() {
        return rating;
    }

    public String getName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getSports() {
        return sports;
    }

    //-----------------------------------------------------------------
    //  Setter methods for all instance variables
    //-----------------------------------------------------------------
    public void setMatches(MatchList matches) {
        this.matches = matches;
    }

    public void setFriendsList(ArrayList<User> friendsList) {
        this.friendsList = friendsList;
    }

    public void setRating(Rate rating) {
        this.rating = rating;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    //-----------------------------------------------------------------
    //  Add new match to "MatchList" of the user object
    //-----------------------------------------------------------------

    public void addMatch(Match match) {
        matches.addMatch(match);
    }

    //-----------------------------------------------------------------
    //  Add new friend to "friendList" of the user object
    //-----------------------------------------------------------------

    public void addFriend(User friend) {
        this.friendsList.add(friend);
    }

    //-----------------------------------------------------------------
    //  Login method to check username & password
    //-----------------------------------------------------------------

    public void login(String userName, String password) {

    }
}
