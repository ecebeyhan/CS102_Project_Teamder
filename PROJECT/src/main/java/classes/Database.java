package classes;

/**
 * THIS CLASS CONTAINS ALL THE DATABASE ACCESS METHODS
 */

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import scenes.MainController;
import scenes.SceneChanger;
import scenes.profileController;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Database {

    private final static String url = "jdbc:postgresql://rogue.db.elephantsql.com:5432/iepnsbnu";
    private final static String username = "iepnsbnu";
    private final static String password = "RucLTf_zMlhMaa99HMxypHICcednwQix";

    public static void main(String[] args) throws SQLException {
        getMatches(getUser("basar123"));
    }

    /**
     * Filter matches according to the user's preferences
     * @param sportPreffered the user's sport preference
     * @param city the user's place
     * @param date the user's date
     * @param matchName the match's name
     * @param resultLabel the label to display the result
     * @return the arraylist of filtered matches
     */
    public static ObservableList<Match> filterMatches(String sportPreffered, String city, LocalDate date, String matchName, Label resultLabel) throws SQLException {
        ObservableList<Match> matches = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            stmt = conn.prepareStatement("SELECT * FROM match WHERE spors = ? AND place = ? AND \"date \" = ? AND name LIKE ?");

            stmt.setString(1, sportPreffered);
            stmt.setString(2, city);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setString(4, "%"+matchName+"%");

            ResultSet rs = stmt.executeQuery();

            Sport preferredSport = null;
            if (sportPreffered.equals("Football")) { preferredSport = new Football(); }
            if (sportPreffered.equals("Basketball")) { preferredSport = new Basketball(); }
            if (sportPreffered.equals("Volleyball")) { preferredSport = new Volleyball(); }
            if (sportPreffered.equals("Tennis")) { preferredSport = new Tennis(); }

            while (rs.next()) {
                int duration = 0;
                duration = (int) Duration.between(rs.getTime("startTime").toLocalTime() , rs.getTime("endTime").toLocalTime()).toMinutes();

                matches.add(new Match( rs.getString("name"),
                        preferredSport, // sport object
                        rs.getString("place"), // place string
                        rs.getDate("date ").toLocalDate(), // date object
                        rs.getTime("startTime").toLocalTime(), // start time object
                        duration)); // duration int
                count++;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        resultLabel.setText("Found " + count + " match(es)");
        return matches;
    }

    // to connect to the database
    public void connect() {
        try {
            Connection myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            Statement myStat = myConnection.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM public.usermatch WHERE name = 'basar123'"); // write sql command
            while (myRs.next()) {
                System.out.println(myRs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get user according to the username
     * @param name the username to get the user for
     */
    public static User getUser(String name) throws SQLException {
        Connection conn = null;
        Statement myStat = null;
        User user = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM users WHERE name = '" + name + "'"); // write sql command
            while (myRs.next()) {
                user = new User(myRs.getString("name"),
                        myRs.getString("password"),
                        myRs.getString("sports"),
                        myRs.getString("bio"),
                        new File(ImageHandler.IMAGE_PATH + myRs.getString("imagefile")));
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (myStat != null) {
                myStat.close();
            }
        }
        return user;
    }

    /**
     * Get matches for the user
     * @param user the user to get the matches for
     */
    public static void getMatches(User user) throws SQLException {
        Connection conn = null;
        Statement myStat = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM public.usermatch WHERE name = '" + user.getName() + "'"); // write sql command
            while (myRs.next()) {
                System.out.println(myRs.getString("matchid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (myStat != null) {
                myStat.close();
            }
        }
    }


    public static Match getMatch(int id) throws SQLException {
        Connection conn = null;
        Statement myStat = null;
        Match match = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM match WHERE matchid = '" + id + "'"); // write sql command
            while (myRs.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (myStat != null) {
                myStat.close();
            }
        }
        return match;
    }
    /**
     * Add a match linked to the user
     * @param user the user to add the match to
     * @param match the match to add
     */
    public static void addMatch(User user, Match match) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            // 1 connect to db
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");

            // 2 create a query ( with ? for user input)
            String query = "INSERT INTO usermatch(name, matchname) VALUES (?, ?)";

            // 3 prepare the statement wanted to run on the sql
            stmt = conn.prepareStatement(query);

            // 4 establish the '?' created in 'step 2'
            stmt.setString(1, user.getName());
            stmt.setString(2, match.getName());

            // 5 execute the query
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    // A Method to add a user to the database
    public static void insert(int ID, String userName, String pass, String sports, String bio) {
        try {
            Connection myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            Statement myStat = myConnection.createStatement();
            myStat.execute("INSERT INTO public.\"user\" (\" ID\", \"userName\", pass, sports, bio) VALUES ('" +
                    ID + "'::smallint, '" + userName + "'::character varying, '" + pass + "'::character varying, '" + sports + "'::character varying, '" + bio + "'::character varying) returning \" ID\";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the user's image in the database.
     * @param image The image to be updated.
     * @param user The user to be updated.
     */
    public static void imageChange(File image, User user) throws SQLException, IOException {
        Connection myConnection = null;
        Statement myStat = null;
        boolean operation = false;
        try {
            myConnection = DriverManager.getConnection(url, username, password);
            myStat = myConnection.createStatement();
            myStat.execute("UPDATE public.\"users\" SET imagefile = '" + image.getName() + "' WHERE \"name\" = '"+ user.getName() + "';");
            operation = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (myConnection != null) {
                myConnection.close();
            }
            if (myStat != null) {
                myStat.close();
            }
        }
        if(operation) {
            user.setImageFile(image);
        }

    }

    /**
     * This method is used to login the user
     * First it connects to the database
     * Then it checks whether the user with the specified username exists
     * If it does, it checks whether the password is correct
     * If it is, it logs in the user
     * Finally, opens the profile page with the user's information
     * @param event The event that triggered the method
     * @param uName The username of the user
     * @param uPass The password of the user
     */
    public static boolean login(String uName, String uPass, ActionEvent event) throws SQLException {
        Connection conn = null;
        PreparedStatement myStatement = null;
        ResultSet resultSet = null;

        try {
            // 1 connect to db
            conn = DriverManager.getConnection(url, username, password);

            // 2 create a query ( with ? for user input)
            String query = "SELECT * FROM users WHERE name = ?";

            // 3 prepare the statement wanted to run on the sql
            myStatement = conn.prepareStatement(query);

            // 4 establish the '?' created in 'step 2'
            myStatement.setString(1, uName);

            // 5 execute the query
            resultSet = myStatement.executeQuery();

            // 6 get passwords from the db compare them with userNames
            String dbPassword = null;
            String sports = null;
            String bio = null;
            File imageFile = null;
            User user = null;

            while (resultSet.next()) {

                dbPassword = resultSet.getString("password");
                sports = resultSet.getString("sports");
                bio = resultSet.getString("bio");

                imageFile = new File(ImageHandler.IMAGE_PATH + resultSet.getString("imagefile"));
                if (imageFile.isFile())
                    user = new User(uName, dbPassword,sports, bio, imageFile);
                else
                    user = new User(uName, dbPassword,sports, bio);
            }
            SceneChanger sc = new SceneChanger();

            if (uPass.equals(dbPassword)) {
                SceneChanger.setLoggedInUser(user);
                MainController controllerClass = new profileController();
                sc.changeScenes(event, "Profile_Page.fxml", "Teamder | Profile Page", user, controllerClass);
                return true;
            } else
                return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (myStatement != null) {
                myStatement.close();
            }

        }
        return false;
    }

    /**
     * Creates a new user and adds it to the database.
     */
    public static void createMatch(String name, Sport sport, String place, LocalDate date, LocalTime startTime, int duration) throws SQLException, IOException {

        Match match = new Match(name, sport, place, date, startTime, duration);
        insertMatchToDB(match); // add into database
        addMatch(SceneChanger.getLoggedInUser(), match);

        SceneChanger sc = new SceneChanger();
        sc.changeScenes(null, "Main_Page.fxml", "Teamder | Main Page", SceneChanger.getLoggedInUser(), null);

    }

    /**
     * Add user to the database
     * First, connect to the database
     * Secondly, create a statement (query with '?' as parameters)
     * Then, put the values into the statement
     * Finally, execute the statement
     */
    public static void insertUserToDB(User u) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. database connection
            conn = DriverManager.getConnection(url, username, password);

            // 2. create a String holding query with ? as user inputs
            String sql = "INSERT INTO users (name, password, sports, bio) VALUES (?, ?, ?, ?);";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getPassword());
            preparedStatement.setString(3, u.getSports());
            preparedStatement.setString(4, u.getBio());

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

    public static void createUser(String uName, String pass, String sport, String bio) throws SQLException, IOException {

        // create the User object with the information from the text fields
        User user = new User(uName, pass, sport, bio);
        insertUserToDB(user); // add into database
    }

    public static void insertMatchToDB(Match match) throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. database connection
            conn = DriverManager.getConnection(url, username, password);

            // 2. create a String holding query with ? as user inputs
            String sql = "INSERT INTO match (name, spors, place, \"date \", \"startTime\", \"endTime\") VALUES (?, ?, ?, ?, ? ,?);";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, match.getName());
            preparedStatement.setString(2, (match.getSport()).getName());
            preparedStatement.setString(3, match.getPlace());
            preparedStatement.setDate(4, Date.valueOf(match.getDate()));
            preparedStatement.setTime(5, Time.valueOf(match.getStartTime()));
            preparedStatement.setTime(6, Time.valueOf(match.endTime()));

            preparedStatement.execute();


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
