import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.ClassNotFoundException;
import java.lang.Exception;
import java.lang.System;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Guestbook{
    public static void main(String[] args) {
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
        
        // if no argument was specified:
        // print the page with all guestbook entries up to now
        try {

            String query = null;

            if (args.length == 0) {
                query = "SELECT * FROM entries";
                pStmt = conn.prepareStatement(query);
                rs = pStmt.executeQuery();
                
                List<String> gbEntries = new ArrayList<String>();
                
                while(rs.next()){
					gbEntries.add(rs.getString("content"));
				}
                			
                // prepare page, containing all guestbook entries	
				String page = "<!DOCTYPE html>";
				page += "<html>";
				page += "<head>";
				page += "<title>World of Guestbook</title>";
				page += "</head>";
				page += "<body>";
				page += "<h1>Guestbook entries</h1>";
				
				// add the Guestbook Entries to the page
				for(String str: gbEntries){
					page += "<p>";
					page += str;
					page += "</p>";
				}
				
				page += "</body>";
				page += "</html>";
				
				// header for HTTP response...
				// scrapped
				/*
				String header = "HTTP/1.1 200 OK\n";
				header += "Connection: close\n";
				header += "Content-Type: text/html; charset=UTF-8\n";
				header += "Content-Length: " + page.length() + "\n";
				header += "\n";
				*/
				
				System.out.println(page);
				
			// if an argument was specified:
			// use it as a new guestbook entry
            }else {
				
				entry = "";
				for(int i=0; i<args.length; i++){
					entry += args[i] + " ";
				}
				entry = entry.trim();
				
				// my mother always told me to sanitize my input
				entry = entry.replaceAll("&", "&amp;")
					.replaceAll("<", "&lt;")
					.replaceAll(">", "&gt;")
					.replaceAll("\'", "&apos;")
					.replaceAll("\"", "&quot;");
								
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
