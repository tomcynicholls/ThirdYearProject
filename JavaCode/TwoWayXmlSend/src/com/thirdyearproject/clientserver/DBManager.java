package com.thirdyearproject.clientserver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

	Connection connection = null;
	String driverName = "com.mysql.jdbc.Driver"; // for MySql
	String serverName = "localhost"; // Use this server.
	String portNumber = "3306";
	String sid = "typdb";
	String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + sid;
	String username = "TYPuser"; // You should modify this.
	String password = "TYPpass"; // You should modify this.

	public DBManager() {

	}

	// public static void main(String[] args) {

	// DBManager con = new DBManager();
	// System.out.println("Connection : " +con.doConnection());
	// System.out.println("IP for ID 2: " +con.returnIPfromID(2));
	// System.out.println("IP for ID 20: " +con.returnIPfromID(20));
	// if (con.returnIPfromID(20) == null) {
	// System.out.println("ERROR");
	// }
	// int addeduser = con.addNewUser("192.168.0.12");
	// System.out.println("Added IP 192.168.0.12 as user: " + addeduser);
	// con.updateUser(addeduser, "ipaddr", "192.168.0.13");
	// System.out.println("Updated user " + addeduser + "with new ip: " +
	// con.returnIPfromID(addeduser));

	// System.out.println("Does ID 30 exist? - " + con.userIDExists(1));

	// }

	public boolean doConnection() {

		try {
			// Load the JDBC driver
			Class.forName(driverName);
			// Create a connection to the database
			connection = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException e) {
			// Could not find the database driver
			System.out.println("ClassNotFoundException : " + e.getMessage());
			return false;
		} catch (SQLException e) {
			// Could not connect to the database
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public String returnIPfromID(int id) {

		String ip = null;
		Statement stmt = null;
		String query = " SELECT * FROM typdb.typusers WHERE id=" + id;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			// while (rs.next()) {
			int ids = rs.getInt(1); // or rs.getString("NAME");
			ip = rs.getString(2);
			String pubkeyloc = rs.getString(3);
			String messloc = rs.getString(4);
			System.out.println(ids + ip + pubkeyloc + messloc);
			// }

			stmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR! 1");
		}

		return ip;

	}

	public String returnFirstMessagefromID(int id, int loc) {

		String ip = null;
		Statement stmt = null;
		String messloc = null;
		String query = " SELECT * FROM typdb.typusers WHERE id=" + id;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			// while (rs.next()) {
			int ids = rs.getInt(1); // or rs.getString("NAME");
			ip = rs.getString(2);
			String pubkeyloc = rs.getString(3);
			messloc = rs.getString(loc);
			//System.out.println(ids + ip + pubkeyloc + messloc);
			// }

			stmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR! 1");
		}

		return messloc;

	}

	public Boolean userIDExists(int id) {

		String query = "SELECT EXISTS (SELECT 1 FROM typdb.typusers WHERE id=" + id + ");";
		Statement stmt;
		Boolean result = null;
		System.out.println(query);
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			System.out.println(rs.getInt(1));

			if (rs.getInt(1) != 0) {
				result = true;
				return result;
			} else {
				result = false;
				return result;
			}

		} catch (SQLException e) {
			System.out.println("ERROR! 5");
		}

		return result;
	}

	public Boolean userIPExists(String ip) {

		String query = "SELECT EXISTS (SELECT 1 FROM typdb.typusers WHERE ipaddr='" + ip + "');";
		Statement stmt;
		Boolean result = null;
		System.out.println(query);
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			System.out.println(rs.getInt(1));

			if (rs.getInt(1) != 0) {
				result = true;
				return result;
			} else {
				result = false;
				return result;
			}

		} catch (SQLException e) {
			System.out.println("ERROR! 5");
		}

		return result;
	}

	public int addNewUser(String ip) {

		Statement stmt = null;
		try {
			// if user does not already exist with that ip address create a new
			// user
			if (!(userIPExists(ip))) {
				stmt = connection.createStatement();
				String sql = "INSERT INTO `typdb`.`typusers` (`ipaddr`) VALUES ('" + ip + "')";
				stmt.executeUpdate(sql);
				stmt.close();
			}
		} catch (SQLException e) {
			System.out.println("ERROR! 2");
		}

		int ids = 0;
		String query = " SELECT * FROM typdb.typusers WHERE ipaddr='" + ip + "'";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			// while (rs.next()) {
			ids = rs.getInt(1); // or rs.getString("NAME");
			// ip = rs.getString(2);
			// String pubkeyloc = rs.getString(3);
			// String messloc = rs.getString(4);
			// System.out.println(ids + ip + pubkeyloc + messloc);
			System.out.println("ID is: " + ids);
			// }

			stmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR! 3");
		}

		// return user id of the (new) user with the given ip address
		return ids;

	}

	public void updateUser(int userid, String field, String data) {

		try {
			Statement stmt = null;
			stmt = connection.createStatement();
			String sql = "UPDATE `typdb`.`typusers` SET `" + field + "`='" + data + "' WHERE `id`='" + userid + "'";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR! 4");
		}

	}
}
