package com.muaavin.webservices.rest;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/TweetQuery")
public class TweetQuery
{
  boolean recordPresent;
  public String Response;
  
  public TweetQuery() {}
  
  @GET
  @Path("/AddTweet")
  @Produces({"application/json"})
  public String addTweet(@QueryParam("User_ID") String user_id, @QueryParam("User_Name") String user_name, @QueryParam("User_ImageUrl") String userProfilePic, @QueryParam("Tweet_ID") String tweet_id, @QueryParam("Message") String message, @QueryParam("ImageUrl") String imageURL, @QueryParam("Group_Name") String group_name, @QueryParam("InfringingUserID") String infringingUser_id, @QueryParam("InfringingUserName") String infringingUserName, @QueryParam("InfringingUserProfilePic") String infringingUserProfilePic)
    throws Exception
  {
    user_id = AesEncryption.decrypt(user_id);tweet_id = AesEncryption.decrypt(tweet_id);
    message = AesEncryption.decrypt(message);imageURL = AesEncryption.decrypt(imageURL);
    infringingUser_id = AesEncryption.decrypt(infringingUser_id);
    infringingUserName = AesEncryption.decrypt(infringingUserName);
    infringingUserProfilePic = AesEncryption.decrypt(infringingUserProfilePic);
    group_name = AesEncryption.decrypt(group_name);user_name = AesEncryption.decrypt(user_name);
    userProfilePic = AesEncryption.decrypt(userProfilePic);
    
    MySqlDb db = new MySqlDb();
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    Response = "Record Already Present";
    
    ResultSet rs = st.executeQuery("select TweetID from TweetTable where TweetID = '" + tweet_id + "'and Group_Name ='" + group_name + "';");
    for (; rs.next(); System.out.println("Tweet Already Present :" + recordPresent)) { recordPresent = true;
    }
    if (!recordPresent)
    {
      String sql = "INSERT INTO TweetTable(TweetID, message, ImageUrl,User_ID,Infringing_User_ID,Group_Name) VALUES ('" + tweet_id + "','" + message + "','" + imageURL + "','" + user_id + "','" + infringingUser_id + "','" + group_name + "');";
      st.executeUpdate(sql);
      System.out.println(" Tweets added Successfully ");
      Response = "Report sent successfully";
    }
    recordPresent = false;
    
    rs = st.executeQuery("select id  from twitterUsers where id = '" + user_id + "' ;");
    for (; rs.next(); System.out.println("User Already Present :" + recordPresent)) recordPresent = true;
    if (!recordPresent) { st.executeUpdate("INSERT INTO TwitterUsers( id,  name, profilePic,state) VALUES ('" + user_id + "','" + user_name + "','" + userProfilePic + "','" + "UnBlocked');");Response = "Report sent successfully"; }
    recordPresent = false;
    

    rs = st.executeQuery("select id from Twitter_InfringingUsers where id = '" + infringingUser_id + "';");
    
    while (rs.next()) { recordPresent = true;
    }
    if (!recordPresent) { st.executeUpdate("INSERT INTO Twitter_InfringingUsers( id,  name, profilePic,state) VALUES ('" + infringingUser_id + "','" + infringingUserName + "','" + infringingUserProfilePic + "','" + "UnBlocked" + "');");recordPresent = false;Response = "Report sent successfully";
    }
    conn.close();return AesEncryption.encrypt(Response);
  }
  

  public ResultSet getResults(String sqlQuery, Statement st)
    throws SQLException
  {
    ResultSet rs = st.executeQuery(sqlQuery);
    return rs;
  }
}
