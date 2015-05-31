import java.lang.ClassNotFoundException;
import java.lang.Exception;
import java.lang.System;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Guestbook{
    public static void main(String[] args) {
	System.out.println(args.length);
	for(String arg: args){
		System.out.println(arg);
	}
	System.out.println("---");
	String entry = null;

        if(args.length > 1){

            System.err.println("Usage: Guestbook [<GUESTBOOK ENTRY TEXT>]");
            System.exit(1);
        }

        Connection conn = null;

        // setting up driver & opening connection
        try{
            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection("jdbc:h2:file:./guestbook", "admin", "");

            PreparedStatement pStmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS entries (id INTEGER PRIMARY KEY AUTO_INCREMENT, content TEXT)");
            pStmt.execute();
            pStmt.close();

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        PreparedStatement pStmt = null;
        ResultSet rs = null;
        // doing database stuff
        try {

            String query = null;

            if (args.length == 0) {
                query = "SELECT * FROM entries";
                pStmt = conn.prepareStatement(query);
                rs = pStmt.executeQuery();
                // TODO send stuff via HTTP

            }else {
                entry = args[0];
                query = "INSERT INTO entries (content) VALUES (?)";

                pStmt = conn.prepareStatement(query);
                pStmt.setString(1, entry);
                pStmt.execute();
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            }catch (Exception e){

            }

            try {
                if (pStmt != null) {
                    pStmt.close();
                }
            }catch (Exception e){

            }

            try {
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e){

            }
        }
    }
}
