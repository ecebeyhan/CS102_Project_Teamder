package classes;

/**
 * THIS CLASS CONTAINS ALL THE DATABASE ACCESS METHODS
 */

import javafx.event.ActionEvent;
import scenes.SceneChanger;
import scenes.profileController;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Database {

    private final static String url = "jdbc:postgresql://rogue.db.elephantsql.com:5432/iepnsbnu";
    private final static String username = "iepnsbnu";
    private final static String password = "RucLTf_zMlhMaa99HMxypHICcednwQix";

    // to connect to the database
    public void connect() {
        try {
            Connection myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            Statement myStat = myConnection.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM public.user"); // write sql command
            while (myRs.next()) {
                System.out.printf("User Name: %s\n Password: %s\n Sports: %s\n Bio: %s\n", myRs.getString("userName"),
                        myRs.getString("pass"), myRs.getString("sports"), myRs.getString("bio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // A Method to add a user to the database
    public void insert(int ID, String userName, String pass, String sports, String bio) {
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
//
                sports = resultSet.getString("sports");
//
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
                profileController controllerClass = new profileController();
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
    public static void createUser(String uName, String pass, String sport, String bio) throws SQLException, IOException {

        // create the User object with the information from the text fields
        User user = new User(uName, pass, sport, bio);
        insertUserToDB(user); // add into database
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
            String sql = "INSERT INTO users (name, password, imagefile, sports, bio) VALUES (?, ?, ?, ?, ?);";

            // 3. create the query
            preparedStatement = conn.prepareStatement(sql);

            // 4. put values into the parameters
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getPassword());
//            preparedStatement.setString(3, imageFile.getName());
            preparedStatement.setString(4, u.getSports());
            preparedStatement.setString(5, u.getBio());

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
