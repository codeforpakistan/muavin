package com.muaavin.webservices.rest;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/posts")
public class Post_Insert_QueryClass
{
  String Post_ID;
  String Group_Name;
  String post_Detail;
  double User_ID;
  Post_Insert_QueryClass[] parr = new Post_Insert_QueryClass[20];
  List<Post_Insert_QueryClass> arr = new ArrayList();
  Post_Insert_QueryClass p;
  
  public Post_Insert_QueryClass() {}
  
  @GET
  @Path("/Insert_Post")
  @Produces({"application/json"})
  public String Insert_Posts(@QueryParam("user_name") String user_name, @QueryParam("UserState") String user_state, @QueryParam("ReportedUserState") String reportedUserState, @QueryParam("Post_id") String P_id, @QueryParam("Group_id") String G_id, @QueryParam("Comment_id") String Comment_id, @QueryParam("PComment_id") String P_Commentid, @QueryParam("Group_name") String G_name, @QueryParam("Profile_name") String profile_name, @QueryParam("user_id") String user_id, @QueryParam("infringing_user_name") String infringinguser_name, @QueryParam("Post_image") String Post_image, @QueryParam("userProfilePic") String userProfilePic, @QueryParam("infringingUser_ProfilePic") String infringingUser_ProfilePic, @QueryParam("Comment") String Comment, @QueryParam("Post_Det") String Post_Det)
    throws Exception
  {
    System.out.println("infringinguser_name " + infringinguser_name);
    user_state = AesEncryption.decrypt(user_state);
    reportedUserState = AesEncryption.decrypt(reportedUserState);
    P_id = AesEncryption.decrypt(P_id);
    G_id = AesEncryption.decrypt(G_id);
    Comment_id = AesEncryption.decrypt(Comment_id);
    P_Commentid = AesEncryption.decrypt(P_Commentid);
    G_name = AesEncryption.decrypt(G_name);
    profile_name = AesEncryption.decrypt(profile_name);
    user_id = AesEncryption.decrypt(user_id);
    userProfilePic = AesEncryption.decrypt(userProfilePic);
    infringinguser_name = AesEncryption.decrypt(infringinguser_name);
    infringingUser_ProfilePic = AesEncryption.decrypt(infringingUser_ProfilePic);
    user_name = AesEncryption.decrypt(user_name);
    Comment = AesEncryption.decrypt(Comment);
    Post_Det = AesEncryption.decrypt(Post_Det);
    Post_image = AesEncryption.decrypt(Post_image);
    
    ResultSet rs = null;
    System.out.println("infringinguser_name " + infringinguser_name);
    MySqlDb Db = new MySqlDb();
    String response = "";
    try
    {
      Connection conn = MySqlDb.connect();
      Statement st = conn.createStatement();
      


      response = "inside try statement :";
      response = "Notified Successfully.";
      
      System.out.print("P_id " + P_id + " G_id " + G_id + " Gname " + G_name + " Profile name " + profile_name + " User ID " + user_id + " Comment_id " + Comment_id + " Parent Comment_id " + P_Commentid + " Post Detail " + Post_Det + " Post image " + Post_image + " User State " + user_state + " InfringingUserState  " + reportedUserState);
      

      try
      {
        int count = 0;
        
        String sql1 = "select * from infringingUsers where User_ID = '" + user_id + "';";
        rs = st.executeQuery(sql1);
        
        if (rs.next())
        {

          count++;
        }
        

        if (count == 0)
        {
          String sql2 = "INSERT INTO infringingUsers(User_ID,Profile_pic, Name, state) VALUES ('" + user_id + "','" + infringingUser_ProfilePic + "','" + infringinguser_name + "','" + reportedUserState + "');";
          st.executeUpdate(sql2);
        }
      }
      catch (Exception e)
      {
        response = " Record Already Present!";
        System.out.println("infringingUsers record already Present");
        response = "Notified Successfully.";
        e.printStackTrace();
      }
      

      try
      {
        int count = 0;
        
        String sql3 = "select * from Users where id = '" + profile_name + "';";
        rs = st.executeQuery(sql3);
        
        if (rs.next())
        {

          count++;
        }
        


        if (count == 0)
        {
          String sql = "INSERT INTO Users(id, name, profilePic, state) VALUES('" + profile_name + "','" + user_name + "','" + userProfilePic + "','" + user_state + "');";
          st.executeUpdate(sql);
        }
        

      }
      catch (Exception e)
      {

        response = "Users Record Already Present!";
        System.out.println("Users record already Present");
        e.printStackTrace();
      }
      


      try
      {
        int count = 0;
        String sql = "select * from postTable where id = '" + P_id + "' and group_name = '" + G_name + "' and User_ID = '" + profile_name + "' and InfringingUserID = '" + user_id + "';";
        rs = st.executeQuery(sql);
        
        while (rs.next())
        {

          count++;
        }
        
        if (count == 0)
        {

          String sql1 = "INSERT INTO postTable(id,group_name, post_Detail,User_ID,InfringingUserID,Post_Image)VALUES('" + P_id + "','" + G_name + "','" + Post_Det + "','" + profile_name + "','" + user_id + "','" + Post_image + "');";
          st.executeUpdate(sql1);
        }
        
      }
      catch (Exception e)
      {
        System.out.println("PostTable Record Already Present!");
        e.printStackTrace();
      }
      
      try
      {
        int count = 0;
        String sql = "select * from Comments where Comment_ID = '" + Comment_id + "' and User_ID = '" + profile_name + "' and Group_Name = '" + G_name + "' and  Parent_Comment_id = '" + P_Commentid + "' and  PostId = '" + Post_ID + "' and InfringingUserId ='" + user_id + "';";
        rs = st.executeQuery(sql);
        
        if (rs.next()) {
          System.out.println("abcd");
          System.out.println("abcd");
          count++;
          response = "Record Already Present!";
        }
        

        if (count == 0)
        {
          System.out.println("xyz");
          String sql1 = "INSERT INTO Comments(User_ID,InfringingUserId, PostId,Parent_Comment_id,Comment_ID,Group_Name, Comment)VALUES('" + profile_name + "','" + user_id + "','" + P_id + "','" + P_Commentid + "','" + Comment_id + "','" + G_name + "','" + Comment + "');";
          st.executeUpdate(sql1);
        }
        
      }
      catch (Exception e)
      {
        response = "Record Already Present!";
        System.out.println("Comments Record Already Present!");
        e.printStackTrace();
      }
      

      try
      {
        int count = 0;
        


        String sql = "select count(Comment_ID) as val from Posts_Comments_Table where Comment_ID = '" + Comment_id + "';";
        rs = st.executeQuery(sql);
        

        while (rs.next()) {
          System.out.println("afdg");
          
          count = rs.getInt("val");
        }
        


        int count1 = 0;
        

        String sql5 = "select count(Post_ID) as val from Posts_Comments_Table where Post_ID = '" + P_id + "';";
        rs = st.executeQuery(sql5);
        

        while (rs.next()) {
          System.out.println("afdg");
          
          count1 = rs.getInt("val");
        }
        





        if ((count == 0) || (count1 == 0))
        {

          String sql6 = "INSERT INTO Posts_Comments_Table(Post_ID,Comment_ID) VALUES('" + P_id + "','" + Comment_id + "');";
          st.executeUpdate(sql6);
        }
        

      }
      catch (Exception e)
      {

        System.out.println("Posts_Comments_Table Record Already Present!");
        e.printStackTrace();
      }
      




      conn.close();
    }
    catch (Exception e) {
      System.out.println("Inside outer try block!");
    }
    

    return AesEncryption.encrypt(response);
  }
}
