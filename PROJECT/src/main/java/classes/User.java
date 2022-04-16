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
        User user = new User("alin", "3111", "tenis", "mbgci ",
                        new File ("./src/main/java/images/denemefoto.png"));
                        System.out.println(user);
        user.insertUserToDB();

    }

    private int userID;         // a uniqe ID for every users
    private String userName;
    private String password;
    private String sports;  // interested sports (gostermelik)
    private String bio;
    private File imageFile;
    private MatchList matches;
    private ArrayList<User> friendsList;
    private Rate rating;
    // private Database db = new Database();

    public User(String name, String password, String sports, String bio) {
        setUserName(name);
        setBio(bio);
        setPassword(password);
        setSports(sports);
        setImageFile(new File("./src/main/java/images/defaultPerson.png"));
    }

    public User(String name, String password, String sports, String bio, File imageFile) throws IOException {
        this(name, password, sports, bio);
        setImageFile(imageFile);
        copyImageFile();
    }


    //-----------------------------------------------------------------
    //  Getter methods for all instance variables
    //-----------------------------------------------------------------
    public ArrayList<User> getFriendsList() {
        return friendsList;
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

    public String getBio() {
        return bio;
    }

    public String getUserName() {
        return userName;
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

    public void setPassword(String password) {
        if (password.length() < 20) 
            this.password = password;
        else
            throw new IllegalArgumentException("Password must be shorter than 20 characters");
    }

    public void setImageFile(File imageFile) {
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
    //  Copy image to specified directory and give a new name
    //-----------------------------------------------------------------
    public void copyImageFile() throws IOException {
        //create a new Path to copy the image into a local directory
        Path sourcePath = imageFile.toPath();
        
        String uniqueFileName = getUniqueFileName(imageFile.getName());
        
        Path targetPath = Paths.get("./src/main/java/images/"+uniqueFileName);
        
        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        //update the imageFile to point to the new File
        imageFile = new File(targetPath.toString());
    }

    //-----------------------------------------------------------------
    // Get unique file name (guarantee uniqueness)
    //-----------------------------------------------------------------
    private String getUniqueFileName(String oldFileName)
    {
        String newName;
        
        //create a Random Number Generator
        SecureRandom rng = new SecureRandom();
        
        //loop until we have a unique file name
        do
        {
            newName = "";
            
            //generate 32 random characters
            for (int count=1; count <=32; count++)
            {
                int nextChar;
                
                do
                {
                    nextChar = rng.nextInt(123);
                } while(!validCharacterValue(nextChar));
                
                newName = String.format("%s%c", newName, nextChar);
            }
            newName += oldFileName;
            
        } while (!uniqueFileInDirectory(newName));
        
        return newName;
    }

    //-----------------------------------------------------------------
    // Check if the directory has a same name file
    //-----------------------------------------------------------------
    public boolean uniqueFileInDirectory(String fileName)
    {
        File directory = new File("./src/main/java/images/");
        
        File[] dir_contents = directory.listFiles();
                
        for (File file: dir_contents)
        {
            if (file.getName().equals(fileName))
                return false;
        }
        return true;
    }

    //-----------------------------------------------------------------
    // Check if the character is a valid character
    // Given corresponds to the ASCII table
    //-----------------------------------------------------------------
    public boolean validCharacterValue(int asciiValue)
    {
        
        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57)
            return true;
        
        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90)
            return true;
        
        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122)
            return true;
        
        return false;
    }

    //-----------------------------------------------------------------
    //  toString method of the user object
    //-----------------------------------------------------------------

    public String toString() {
        return String.format("%s: %s %s: %s %s: %s %s: %s",
         "User Name: " , getUserName(), " Password: " , getPassword(), " Sports: " , getSports(), " Bio: " , getBio());
    }

    //-----------------------------------------------------------------
    //  Add user object to the database
    //-----------------------------------------------------------------
    public void insertUserToDB() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String url = "jdbc:postgresql://rogue.db.elephantsql.com:5432/iepnsbnu";
        String username = "iepnsbnu";
        String pass = "RucLTf_zMlhMaa99HMxypHICcednwQix";
        try {
            // 1. database connection
            conn = DriverManager.getConnection(url, username, pass);

            // 2. create a String holding query with ? as user inputs
            String sql = "INSERT INTO users (name, password, imagefile, sports, bio) VALUES (?, ?, ?, ?, ?);";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, imageFile.getName());
            preparedStatement.setString(4, sports);
            preparedStatement.setString(5, bio);

            preparedStatement.executeUpdate();  


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

    }
}
