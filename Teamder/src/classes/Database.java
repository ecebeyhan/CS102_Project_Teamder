package classes;

import java.sql.*;

class Database {

    private String url = "jdbc:postgresql://rogue.db.elephantsql.com:5432/iepnsbnu";
    private String username = "iepnsbnu";
    private String password = "RucLTf_zMlhMaa99HMxypHICcednwQix";

    // to connect to the database
    public void connect() {
        try {
            Connection myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            Statement myStat = myConnection.createStatement();
            ResultSet myRs = myStat.executeQuery("SELECT * FROM public.user"); // write sql command
            while (myRs.next()) {
                System.out.printf("User Name: %s\n Password: %s\n Sports: %s\n Bio: %s\n",  myRs.getString("userName"),
                                                             myRs.getString("pass"), myRs.getString("sports"), myRs.getString("bio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // A Method to add a user to the database
    public void insert(int ID,String userName, String pass, String sports, String bio) {
        try {
            Connection myConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!!");
            Statement myStat = myConnection.createStatement();
            myStat.execute("INSERT INTO public.\"user\" (\" ID\", \"userName\", pass, sports, bio) VALUES ('" +
             ID + "'::smallint, '"+ userName +"'::character varying, '"+ pass +"'::character varying, '"+ sports +"'::character varying, '"+ bio +"'::character varying) returning \" ID\";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Database().insert(15, "test", "test", "test", "test"); // ID part cannot be same as the one in the database
    }
}
