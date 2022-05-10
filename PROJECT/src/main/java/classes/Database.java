
package classes;

/**
 * THIS CLASS CONTAINS ALL THE DATABASE ACCESS METHODS
 */

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import scenes.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Database {

    private final static String url = "jdbc:postgresql://rogue.db.elephantsql.com:5432/iepnsbnu";
    private final static String username = "iepnsbnu";
    private final static String password = "RucLTf_zMlhMaa99HMxypHICcednwQix";

    private static Connection conn;

    public Database() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObservableList<Match> alternativeFilter(String sportPreffered, String city, String matchName, Label resultLabel) {
        ObservableList<Match> matches = FXCollections.observableArrayList();
        PreparedStatement stmt = null;
        int count = 0;

        try {
            stmt = conn.prepareStatement("SELECT * FROM match WHERE spors LIKE ? AND place LIKE ? AND name LIKE ?");

            stmt.setString(1, sportPreffered);
            stmt.setString(2, "%" + city + "%");
            stmt.setString(3, "%" + matchName + "%");

            ResultSet rs = stmt.executeQuery();

            Sport preferredSport = null;
            if (sportPreffered.equals("Football")) {
                preferredSport = new Football();
            }
            if (sportPreffered.equals("Basketball")) {
                preferredSport = new Basketball();
            }
            if (sportPreffered.equals("Volleyball")) {
                preferredSport = new Volleyball();
            }
            if (sportPreffered.equals("Tennis")) {
                preferredSport = new Tennis();
            }
            while (rs.next()) {
                int duration = 0;
                duration = (int) Duration.between(rs.getTime("startTime").toLocalTime(), rs.getTime("endTime").toLocalTime()).toMinutes();

                matches.add(new Match(rs.getString("name"),
                        preferredSport, // sport object
                        rs.getString("place"), // place string
                        rs.getDate("date ").toLocalDate(), // date object
                        rs.getTime("startTime").toLocalTime(), // start time object
                        duration)); // duration int
                count++;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        for (Match match : matches) {
            if (match.getDate().isBefore(LocalDate.now())) {
                count--;
            }
        }
        matches.removeIf(match -> match.getDate().isBefore(LocalDate.now()));
        resultLabel.setText("Found " + count + " match(es)");
        return matches;
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
        PreparedStatement stmt = null;
        int count = 0;
        try {
            stmt = conn.prepareStatement("SELECT * FROM match WHERE spors = ? AND place LIKE ? AND \"date \" = ? AND name LIKE ?");

            stmt.setString(1, sportPreffered);
            stmt.setString(2, "%" + city + "%");
            stmt.setDate(3, Date.valueOf(date));
            stmt.setString(4, "%" + matchName + "%");

            ResultSet rs = stmt.executeQuery();

            Sport preferredSport = null;
            if (sportPreffered.equals("Football")) {
                preferredSport = new Football();
            }
            if (sportPreffered.equals("Basketball")) {
                preferredSport = new Basketball();
            }
            if (sportPreffered.equals("Volleyball")) {
                preferredSport = new Volleyball();
            }
            if (sportPreffered.equals("Tennis")) {
                preferredSport = new Tennis();
            }

            while (rs.next()) {
                int duration = 0;
                duration = (int) Duration.between(rs.getTime("startTime").toLocalTime(), rs.getTime("endTime").toLocalTime()).toMinutes();

                matches.add(new Match(rs.getString("name"),
                        preferredSport, // sport object
                        rs.getString("place"), // place string
                        rs.getDate("date ").toLocalDate(), // date object
                        rs.getTime("startTime").toLocalTime(), // start time object
                        duration)); // duration int
                count++;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        resultLabel.setText("Found " + count + " match(es)");
        return matches;
    }

    /**
     * Get user according to the username
     * @param name the username to get the user for
     */
    public static User getUser(String name) throws SQLException {
        Statement myStat = null;
        User user = null;
        try {
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM users WHERE name = '" + name + "'"); // write sql command
            while (myRs.next()) {
                String username = myRs.getString("name");
                String dbPassword = myRs.getString("password");
                String sports = myRs.getString("sports");
                String bio = myRs.getString("bio");

                File imageFile = new File(ImageHandler.IMAGE_PATH + myRs.getString("imagefile"));
                if (imageFile.isFile())
                    user = new User(username, dbPassword,sports, bio, imageFile);
                else
                    user = new User(username, dbPassword,sports, bio);

                double rating = myRs.getDouble("rate");
                int noOfRaters = myRs.getInt("noofraters");
                double rate = 0;
                if(noOfRaters > 0){
                    rate = Rate.update(rating, noOfRaters);
                }
                user.setRating(rate);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Get all matches from the database
     * @return a list of all matches
     */
    public static ArrayList<String> getAllMatches() throws SQLException {
        ArrayList<String> matches = new ArrayList<>();
        Statement myStat = null;
        try {
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM match"); // write sql command
            while (myRs.next()) {
                matches.add(myRs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    /**
     * Get matches for the user
     * @param user the user to get the matches for
     * @return the name of the matches
     */
    public static ArrayList<String> getMatches(User user) throws SQLException {
        ArrayList<String> matches = new ArrayList<>();
        Statement myStat = null;
        try {
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM usermatch WHERE name = '" + user.getName() + "'"); // write sql command
            while (myRs.next()) {
                matches.add(myRs.getString("matchname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public static ArrayList<String> getPlayersFromMatch(Match match) throws SQLException {
        ArrayList<String> players = new ArrayList<>();
        Statement myStat = null;
        try {
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM usermatch WHERE matchname = '" + match.getName() + "'"); // write sql command
            while (myRs.next()) {
                players.add(myRs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    /**
     * Get active matches of the user
     * @param user the user to get the active matches for
     * @return the active matches
     */
    public static ObservableList<Match> getActiveMatches(User user) throws SQLException {
//        matchActivity(); // some delay to make sure the matches are updated
        ObservableList<Match> matches = FXCollections.observableArrayList();
        ArrayList<String> allMatches = getMatches(user);
        ArrayList<String> activeMatches = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet myRs = null;
        try {
            for (String match : allMatches) {
                st = conn.prepareStatement("SELECT * FROM match WHERE name = ? AND active = ?");
                st.setString(1, match);
                st.setBoolean(2, true);

                myRs = st.executeQuery();

                while (myRs.next()) {
                    activeMatches.add(myRs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String match : activeMatches) {
            matches.add(getMatch(match));
        }
        return matches;
    }

    /**
     * Get inactive matches of the user
     * @param user the user to get the inactive matches for
     * @return the inactive matches
     */
    public static ObservableList<Match> getInactiveMatches(User user) throws SQLException {
        ObservableList<Match> matches = FXCollections.observableArrayList();
        ArrayList<String> allMatches = getMatches(user);
        ArrayList<String> inActiveMatches = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet myRs = null;
        try {
            for (String match : allMatches) {
                st = conn.prepareStatement("SELECT * FROM match WHERE name = ? AND active = ?");
                st.setString(1, match);
                st.setBoolean(2, false);

                myRs = st.executeQuery();

                while (myRs.next()) {
                    inActiveMatches.add(myRs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String match : inActiveMatches) {
            matches.add(getMatch(match));
        }
        return matches;
    }

    /**
     * Get the match according to the name of the match
     * @param name the name of the match to get the match for
     * @return the match object
     */
    public static Match getMatch(String name) throws SQLException {
        Statement myStat = null;
        Match match = null;
        Sport preferredSport = null;
        String sportPreferred = null;

        try {
            myStat = conn.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM match WHERE name = '" + name + "'"); // write sql command

            while (myRs.next()) {
                int duration = 0;
                duration = (int) Duration.between(myRs.getTime("startTime").toLocalTime() , myRs.getTime("endTime").toLocalTime()).toMinutes();

                sportPreferred = myRs.getString("spors");
                if (sportPreferred.equals("Football")) { preferredSport = new Football(); }
                if (sportPreferred.equals("Basketball")) { preferredSport = new Basketball(); }
                if (sportPreferred.equals("Volleyball")) { preferredSport = new Volleyball(); }
                if (sportPreferred.equals("Tennis")) { preferredSport = new Tennis(); }

                match = new Match(myRs.getString("name"),
                        preferredSport,
                        myRs.getString("place"),
                        myRs.getDate("date ").toLocalDate(),
                        myRs.getTime("startTime").toLocalTime(),
                        duration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return match;
    }
    /**
     * Add a match linked to the user
     * @param user the user to add the match to
     * @param match the match to add
     */
    public static void addMatch(User user, Match match, int position) throws SQLException {
        PreparedStatement stmt = null;

        try {
            // 2 create a query ( with ? for user input)
            String query = "INSERT INTO usermatch(name, matchname,position ) VALUES (?, ?, ?)";

            // 3 prepare the statement wanted to run on the sql
            stmt = conn.prepareStatement(query);

            // 4 establish the '?' created in 'step 2'
            stmt.setString(1, user.getName());
            stmt.setString(2, match.getName());
            stmt.setInt(3, position);

            // 5 execute the query
            stmt.executeUpdate();

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
        Statement myStat = null;
        boolean operation = false;
        try {
            myStat = conn.createStatement();
            myStat.execute("UPDATE public.\"users\" SET imagefile = '" + image.getName() + "' WHERE \"name\" = '"+ user.getName() + "';");
            operation = true;
        } catch (SQLException e) {
            e.printStackTrace();
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
        PreparedStatement myStatement = null;
        ResultSet resultSet = null;

        try {

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

                double rating = resultSet.getDouble("rate");
                int noOfRaters = resultSet.getInt("noofraters");
                double rate = 0;
                if(noOfRaters > 0){
                    rate = Rate.update(rating, noOfRaters);
                }
                user.setRating(rate);
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
        }
        return false;
    }

    public static boolean isVoted(User user, Match match){
        PreparedStatement myStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT israted FROM usermatch WHERE name = ? AND matchname = ?;";

            myStatement = conn.prepareStatement(query);

            myStatement.setString(1, user.getName());

            myStatement.setString(2, match.getName());

            resultSet = myStatement.executeQuery();

            boolean rated = false;
            if (resultSet.next()) {
                rated = resultSet.getBoolean("israted");
            }
            return rated;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    /**
     * Creates a new user and adds it to the database.
     */
    public static void createMatch(ActionEvent event, String name, Sport sport, String place, LocalDate date, LocalTime startTime, int duration) throws SQLException, IOException {

        Match match = new Match(name, sport, place, date, startTime, duration);
        insertMatchToDB(match); // add into database

        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Match_Page.fxml", "Teamder | Match Page", SceneChanger.getLoggedInUser(), match, new matchPageController(), new matchPageController());
    }

    /**
     * Add user to the database
     * First, connect to the database
     * Secondly, create a statement (query with '?' as parameters)
     * Then, put the values into the statement
     * Finally, execute the statement
     */
    public static void insertUserToDB(User u) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            // 2. create a String holding query with ? as user inputs
            String sql = "INSERT INTO users (name, password, sports, bio, imagefile) VALUES (?, ?, ?, ?, ?);";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getPassword());
            preparedStatement.setString(3, u.getSports());
            preparedStatement.setString(4, u.getBio());
            preparedStatement.setString(5, "defaultPerson.png");

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createUser(String uName, String pass, String sport, String bio) throws SQLException, IOException {
        // create the User object with the information from the text fields
        User user = new User(uName, pass, sport, bio);
        insertUserToDB(user); // add into database
    }

    public static void insertMatchToDB(Match match) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
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
        }
    }

    public static void matchActivity() throws SQLException {
        PreparedStatement preparedStatement = null;
        LocalDateTime now = LocalDateTime.now();

        //matches properties for comparison
        LocalTime endTime = null;
        LocalDate matchDate = null;

        ArrayList<String> matchName = new ArrayList<>(); // matches to be set inactive

        try {
            // 2. create a String holding query
            String sql = "SELECT * FROM match";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. execute the query
            ResultSet rs = preparedStatement.executeQuery();

            // 5. print the results
            while (rs.next()) {
                endTime = rs.getTime("endTime").toLocalTime();
                matchDate = rs.getDate("date ").toLocalDate();
                if (now.toLocalDate().isAfter(matchDate) || (now.toLocalDate().equals(matchDate) && now.toLocalTime().isAfter(endTime)) ){ // compare if match's date and time with now
                    matchName.add(rs.getString("name")); // add to list of matches to be set inactive
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (String name : matchName) {
            setMatchInactive(name);
        }
    }

    /**
     * Set match inactive
     * @param name match name
     */
    public static void setMatchInactive(String name) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            // 2. create a String holding query
            String sql = "UPDATE match SET active = false WHERE name = ?;";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, name);

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setRateInactive(String name, String matchName) throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            // 2. create a String holding query
            String sql = "UPDATE usermatch SET israted = true WHERE name = ? AND matchname = ?;";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, matchName);

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getFriends(User user) throws SQLException {
        ObservableList<User> friends = FXCollections.observableArrayList();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("SELECT user2 FROM useruser WHERE user1 = ?");

            stmt.setString(1, user.getName());

            ResultSet rs = stmt.executeQuery();

            ArrayList<String> friendUsernames = new ArrayList<>();
            while (rs.next()) {
                String friendUsername = rs.getString("user2");
                friendUsernames.add(friendUsername);
            }

            for (String friendUsername : friendUsernames) {
                User friend = getUser(friendUsername);
                friends.add(friend);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static boolean isUserInMatch(String uName,String matchName) throws SQLException
    {
        PreparedStatement myStatement = null;
        ResultSet resultSet = null;

        try {
            // 2 create a query ( with ? for match input)
            String query = "SELECT * FROM usermatch WHERE matchname = ?";

            // 3 prepare the statement wanted to run on the sql
            myStatement = conn.prepareStatement(query);

            // 4 establish the '?' created in 'step 2'
            myStatement.setString(1, matchName);

            // 5 execute the query
            resultSet = myStatement.executeQuery();

            // 6 get all users of the match from the db, check if one of them is the user
            while (resultSet.next()) {
                if(resultSet.getString("name").equals(uName))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static User getUserInPosition(String matchName, int position) throws SQLException
    {
        PreparedStatement myStatement = null;
        ResultSet resultSet = null;
        try {
            // 2 create a query ( with ? for match input)
            String query = "SELECT * FROM usermatch WHERE matchname = ?";

            // 3 prepare the statement wanted to run on the sql
            myStatement = conn.prepareStatement(query);

            // 4 establish the '?' created in 'step 2'
            myStatement.setString(1, matchName);

            // 5 execute the query
            resultSet = myStatement.executeQuery();

            // 6 get the user in the match in wanted position
            while (resultSet.next()) {
                if(resultSet.getInt("position") == position)
                {
                    String name = resultSet.getString("name");
                    return Database.getUser(name);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void updateRatings(int value, String playerName){
        PreparedStatement preparedStatement = null;

        try {
            // 2. create a String holding query with ? as user inputs
            String sql = "UPDATE users SET rate = rate + ? , noofraters = noofraters + 1  WHERE name = ?;";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, playerName);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFriend(User user1, User user2)throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            // 2. create a String holding query with ? as user inputs
            String sql = "INSERT INTO useruser (user1, user2) VALUES (?, ?);";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, user1.getName());
            preparedStatement.setString(2, user2.getName());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeFriend(User user1, User user2)throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            // 2. create a String holding query with ? as user inputs
            String sql = "DELETE FROM useruser WHERE user1= ? and user2= ?;";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, user1.getName());
            preparedStatement.setString(2, user2.getName());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters today's matches according to their starting time
     * @param sportPreffered the user's sport preference
     * @param city the user's place
     * @param date the user's date
     * @param matchName the match's name
     * @param resultLabel the label to display the result
     * @return the arraylist of filtered matches
     * @throws SQLException
     */
    public static ObservableList<Match> filterTodaysMatches(String sportPreffered, String city, LocalDate date, String matchName, Label resultLabel) throws SQLException {
        ObservableList<Match> matches = FXCollections.observableArrayList();
        matches = filterMatches(sportPreffered, city, date, matchName, resultLabel );
        LocalDateTime now = LocalDateTime.now();
        int pos = 0;
        while( pos < matches.size()){
            if(now.toLocalTime().isAfter(matches.get(pos).getStartTime())){
                matches.remove(matches.get(pos));
            }
            else{
                pos++;
            }
        }
        resultLabel.setText("Found " + matches.size() + " match(es)");
        return matches;
    }

        /**
     * Checks whether user can join a match since user can have another match at that time
     * @param newMatch match that user wants to join and that will be checked
     * @return true if user can join, false otherwise
     * @throws SQLException
     */
    public static boolean canUserJoinMatch(Match newMatch) throws SQLException {
        ObservableList<Match> usersAlreadyMatches = FXCollections.observableArrayList();
        usersAlreadyMatches = Database.getActiveMatches(SceneChanger.loggedInUser);
        for( Match m : usersAlreadyMatches){
            if( newMatch.getDate().equals(m.getDate()) && (m.getStartTime().isAfter(newMatch.getStartTime()) || m.getStartTime().equals(newMatch.getStartTime()) ) && newMatch.endTime().isAfter(m.getStartTime())){
                return false;
            }
            else if(newMatch.getDate().equals(m.getDate()) && newMatch.getStartTime().isAfter(m.getStartTime()) && m.endTime().isAfter(newMatch.getStartTime())){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether there is already a match whose name is same as the parameter
     * @param newMatchName name of the match that is intended to be created
     * @return true if the name is already used, false otherwise
     * @throws SQLException
     */
    public static boolean doesMatchNameExist(String newMatchName) throws SQLException {
        ArrayList<String> matches = new ArrayList<>();
        matches = Database.getAllMatches();
        for( String name: matches){
            if( newMatchName.equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether there is already exist whose name is same as the parameter
     * @param username name of the user that is intended to be created
     * @return true if the name is already used, false otherwise
     */
    public static boolean isUserExist(String username) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");

            stmt.setString(1, username);

            rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("name").equals(username)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void getMessage(String matchName, TextArea tArea) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        tArea.clear();
        try {
            stmt = conn.prepareStatement("SELECT * FROM message WHERE \"group\" = ?");
            stmt.setString(1, matchName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                tArea.appendText(rs.getString("sender") + ": " + rs.getString("messages") + "\n");
                tArea.setScrollTop(Double.MAX_VALUE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessage(String msg, Match match,TextArea tArea) {
        PreparedStatement stmt = null;

        try {
            String query = "INSERT INTO public.message(sender, \"group\", messages) VALUES (?, ?, ?)";

            stmt = conn.prepareStatement(query);

            stmt.setString(1, SceneChanger.getLoggedInUser().getUserName());
            stmt.setString(2, match.getName());
            stmt.setString(3, msg);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tArea.appendText(SceneChanger.getLoggedInUser().getUserName() + ": " + msg + "\n");
        tArea.setScrollTop(Double.MAX_VALUE);
    }
}

