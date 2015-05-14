import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for project lab1c-app0.
 *
 * @author Marcel Gredler
 */
public class SQLInjection {

    /**
     * args[0] name of the person
     *
     * @param args name of the new person
     */
    public static void main(String[] args){

		System.out.println(args[0]);
        if(args.length < 1){
            System.err.println("Usage: java -jar SQLInjection <name to add>");
            System.exit(-1);
        }
        String name = args[0];
        Connection conn = null;
        String add_person_to_db = "INSERT INTO person(name) VALUES (";

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

                Statement stat = conn.createStatement();
                boolean retval = stat.execute(add_person_to_db+"'"+name+"')");
                System.out.println("Added Person:<"+name+"> to database.");
                stat.close();
            } catch (SQLException e) {
                System.out.println("Invalid input \nUsage: java -jar SQLInjection <name to add>\n"+e.getMessage());
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
