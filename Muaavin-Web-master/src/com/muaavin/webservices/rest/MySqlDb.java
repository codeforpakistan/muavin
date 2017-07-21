package com.muaavin.webservices.rest;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDb {

	
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/muaavin";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "root";


	

public static Connection connect(){
	Connection  conn = null;
	try {
		Class.forName("com.mysql.jdbc.Driver");

		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	return conn;
}

}
