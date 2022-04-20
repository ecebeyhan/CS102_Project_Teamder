package classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class User {

    public static void main(String[] args) throws IOException, SQLException {
        User user = new User("basar", "3111", "tenis", "mbgci ",
                        new File ("./src/main/java/images/denemefoto.png"));
                        System.out.println(user);
        Database.insertUserToDB(user);

    }

    private int userID;         // a uniqe ID for every users
    private String userName;
    private String password;
    private String sports;  // interested sports (gostermelik)
    private String bio;
    private File imageFile;
    private ArrayList<User> friendsList;
    private Rate rating;

    /**
     * Create a new user with the given name, password, sports and bio.
     * IMPORTANT: Image file is default one (defaultPerson.png)
     * @param name the username of the user
     * @param password the password of the user
     * @param sports the interested sports of the user
     * @param bio the bio of the user
     */
    public User(String name, String password, String sports, String bio) throws IOException {
        setUserName(name);
        setBio(bio);
        setPassword(password);
        setSports(sports);
        setImageFile(new File("./src/main/java/images/defaultPerson.png"));
    }

    /**
     * Create a new user with the given name, password, sports, bio and imageFile.
     * @param name the username of the user
     * @param password the password of the user
     * @param sports the interested sports of the user
     * @param bio the bio of the user
     * @param imageFile the image of the user
     */
    public User(String name, String password, String sports, String bio, File imageFile) throws IOException {
        this(name, password, sports, bio);
        setImageFile(imageFile);
    }

    //-----------------------------------------------------------------
    //  Getter methods for all instance variables
    //-----------------------------------------------------------------
    public ArrayList<User> getFriendsList() {
        return friendsList;
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

    public String getBio() {
        return bio;
    }

    public String getUserName() {
        return userName;
    }

    public File getImageFile() {
        return imageFile;
    }
    
    //-----------------------------------------------------------------
    //  Setter methods for all instance variables
    //-----------------------------------------------------------------
    public void setFriendsList(ArrayList<User> friendsList) {
        this.friendsList = friendsList;
    }

    public void setRating(Rate rating) {
        this.rating = rating;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPassword(String password) {
        if (password.length() < 20) 
            this.password = password;
        else
            throw new IllegalArgumentException("Password must be shorter than 20 characters");
    }

    public void setImageFile(File imageFile) throws IOException {
        this.imageFile = imageFile;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void addFriend(User friend) {
        this.friendsList.add(friend);
    }



    /**
     * toString method
     * @return a string representation of the User class
     */
    public String toString() {
        return String.format("%s: %s %s: %s %s: %s %s: %s",
         "User Name: " , getUserName(), " Password: " , getPassword(), " Sports: " , getSports(), " Bio: " , getBio());
    }

}
