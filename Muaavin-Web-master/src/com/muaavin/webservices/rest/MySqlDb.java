package com.muaavin.webservices.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;







public class MySqlDb
{
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/muaavin";
  static final String USER = "root";
  static final String PASS = "root";
  
  public MySqlDb() {}
  
  public static Connection connect()
  {
	  Connection conn = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      try
      {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/muaavin", "root", "root");
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
      





    }
    catch (ClassNotFoundException e1)
    {
      e1.printStackTrace();
    }      return conn;
  }
}
