import java.sql.*;

/**
 * Class for project lab1c-app0.
 *
 * @author Marcel Gredler
 */
public class SQLInjectionFix {

    /**
     * args[0] name of the person
     *
     * @param args name of the new person
     */
    public static void main(String[] args){

        if(args.length != 1){
            System.err.println("Usage: java -jar SQLInjection <name to add>");
            System.exit(-1);
        }
        String name = args[0];
        Connection conn = null;
        String add_person_to_db = "INSERT INTO person(name) VALUES (?)";

        try{
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Cant' find Driver for H2-Database");
            System.exit(-1);
        }
        try {
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/app0", "admin", "");
        } catch (SQLException e) {
            System.err.println("Cannot create connection from Manager.");
            System.exit(-1);
        }

        if(conn != null){

            try {

                PreparedStatement stat = conn.prepareStatement(add_person_to_db);
                stat.setString(1,name);
                boolean retval = stat.execute();
                System.out.println("Added Person:<"+name+"> to database.");
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }

        if(conn != null){

            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Couldn't close DB-connection.");
            }

        }

        System.exit(1);
    }
}
