package com.tobee.prepare.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SqlcipherTest {

	private static File testdb = new File("resources/db/mydb.db");
	
	private static final String QUERY_GPS_DATA = 
		"SELECT USR_ID, IMEI, LAT, LON, GDATE,GTIME " + 
			"FROM TSMS_GPS_TRACK";
	public static void forName() throws Exception {
		String sqliteNativeLibraryPath = "C:/DEV/COMP/msys32/home/tobee/sqlciper-jdbc/bin";
		String sqliteNativeLibraryName = "sqlitejdbc.dll";
		System.setProperty("org.sqlite.lib.path", sqliteNativeLibraryPath);
		System.setProperty("org.sqlite.lib.name", sqliteNativeLibraryName);
		
        Class.forName("org.sqlite.JDBC");
    }
	
	private static Connection getConnection() throws SQLException {
		if(!testdb.exists())
			System.out.println("test db doesn't exist...!!!");
		else
			System.out.println("test db exists...!!!");
		
		String url = "jdbc:sqlite:" + testdb.getAbsolutePath();

		Properties props = new Properties();
		//final String password = "mypassword";
		
		//props.put("key", password);
		Connection conn = DriverManager.getConnection(url, props);
		
		return conn;
	}

	private static void queryCommnGPSData() {
		Connection conn = null;
		
		try {
			// create a database connection
			conn = getConnection();
			conn.setAutoCommit(true);
			
			Statement statement = conn.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			//conn.crea
			statement.execute("PRAGMA key ='mypassword'");
			
			ResultSet rs = statement.executeQuery(QUERY_GPS_DATA);
			
			while (rs.next()) {
				System.out.println("USR_ID: " + rs.getString(1));
				System.out.println("IMEI: " + rs.getString(2));
				System.out.println("LAT: " + rs.getString(3));
				System.out.println("LON: " + rs.getString(4));
				System.out.println("GDATE: " + rs.getString(5));
				System.out.println("GTIME: " + rs.getString(6));
			}
			

		} catch (SQLException e) {
			
			//System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}
	
	public static void main(String[] args)
	{
		try {
			forName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		queryCommnGPSData();
	}
}
