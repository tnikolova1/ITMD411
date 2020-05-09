//Final Project Teodora Nikolova
package final_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Dao {
	
	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}


	
	
	static Connection connect = null;
	Statement statement = null;
	
	public void createTable() {
		 try {
		 
		statement = getConnection().createStatement();
	
			String sql = "CREATE TABLE TeodoraJava_tickets" + 
			             "(tic_num INTEGER not NULL AUTO_INCREMENT, " +
			  	       "userid VARCHAR(20), " +
					 " tic_desc VARCHAR(100), " + 
					 " tic_status VARCHAR(20), " + 
					 " startdate DATE, " + 
					 " enddate DATE, " +
					 " tic_reply VARCHAR(100), " + 
				 " PRIMARY KEY ( tic_num ))";
	
		statement.executeUpdate(sql);
		
		String sql2 = "CREATE TABLE TeodoraJava_users" + 
		             "(userid VARCHAR(20) not NULL, " +
		  	       " passw VARCHAR(20), " +
		           " admin BOOLEAN, " + 
			 " PRIMARY KEY ( userid ))";

		statement.executeUpdate(sql2);
		
		getConnection().close(); //close db connection 
		} catch (SQLException se) {
		// Handle errors for JDBC
		 se.printStackTrace();
		}
	}
	public void addUsers()  {
	
	// add list of users from userlist.csv file to users table

			// variables for SQL Query inserts
			String sql;

			Statement statement;
			BufferedReader br;
			List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

			// read data from file
			try {
				br = new BufferedReader(new FileReader(new File("userlist.csv")));

				String line;
				while ((line = br.readLine()) != null) {
					array.add(Arrays.asList(line.split(",")));
				}
			} catch (Exception e) {
				System.out.println("There was a problem loading the file");
			}

			try {

				// Setup the connection with the DB

				statement = getConnection().createStatement();

				// create loop to grab each array index containing a list of values
				// and PASS (insert) that data into your User table
				for (List<String> rowData : array) {

					sql = "insert into TeodoraJava_users(userid,passw,admin) " + "values('" + rowData.get(0) + "'," + " '"
							+ rowData.get(1) + "','" + rowData.get(2) + "');";
					statement.executeUpdate(sql);
				}
				System.out.println("Inserts completed in the given database...");

				// close statement object
				statement.close();

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	//CRUD
	
	//Insert a ticket
	public void insertTicket(String userid, String t_Desc) {
		try {
			statement = getConnection().createStatement();
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
			statement.executeUpdate("Insert into TeodoraJava_tickets" + 
					"(userid, tic_desc, startdate, tic_status) values(" +
					" '" + userid + "','" + t_Desc + "','" +
					timeStamp  + "','" + "OPEN" + "')");
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//View all ticket for JTable
	public ResultSet readTickets(String userid) {

		ResultSet results = null;
		try {
			statement = getConnection().createStatement();
			results = statement.executeQuery("SELECT * FROM TeodoraJava_tickets WHERE userid = " + "'" + userid + "'");
			getConnection().close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	
	//Update a ticket
	public void updateTicket(String uid, int tid, String nDesc) {
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("UPDATE TeodoraJava_tickets SET tic_desc = " + 
			"'" + nDesc + "'" + " WHERE tic_num = " + tid +
			" AND userid = " + "'" + uid + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Delete a ticket
	public void deleteTicket(String uid, int tid) {
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("DELETE FROM TeodoraJava_tickets WHERE userid = " + 
			"'" + uid + "'" + " AND tic_num = " + tid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Read a specific ticket.
	public String readTicket(String uid, int tid) {
		ResultSet resultSet = null;
		String tdesc = null;
		String sTime = null;
	    String eTime = null;
		String status= null;
		try {
			statement = getConnection().createStatement();
			resultSet = statement.executeQuery("SELECT * FROM TeodoraJava_tickets WHERE userid = " + 
			"'" + uid + "'" + " AND tic_num = " + tid);
			while(resultSet.next()) {
				tdesc = resultSet.getString("tic_desc");
				sTime = resultSet.getString("startdate");
			    eTime= resultSet.getString("enddate");
				status= resultSet.getString("tic_status");
			}
			
			//Construct output for dialog
			String result = "Ticket: " + tid + "\nDescription:" + tdesc + "\nStart Time: " + sTime + "End Time: " + eTime + "\nStatus: " + status;
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Could not read";
	}
	
}
