package com.muaavin.webservices.rest;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;








@Path("/UsersPosts")
public class UsersPosts
{
  List<Post> Post_List = new ArrayList();
  boolean isPostOfSpecificUser;
  
  public UsersPosts() {}
  
  @GET
  @Path("/GetUsersPosts")
  @Produces({"application/json"})
  public String getUsersPosts(@QueryParam("name") String Group_name, @QueryParam("user_id") String user_id, @QueryParam("isSpecificUserPost") boolean isPostOfSpecificUser, @QueryParam("TwitterUserID") String TwitterUserID) throws Exception { if (Group_name != null) { Group_name = AesEncryption.decrypt(Group_name);
    }
    if (user_id != null) { user_id = AesEncryption.decrypt(user_id);
    }
    if (TwitterUserID != null) { TwitterUserID = AesEncryption.decrypt(TwitterUserID);
    }
    MySqlDb db = new MySqlDb();
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    ResultSet rs = null;
    
    if (isPostOfSpecificUser)
    {
      rs = getUserPosts(Group_name, rs, st, user_id, TwitterUserID, true, false);
      Post_List = getresultantDataFromDB(rs, true, new ArrayList(), false);
      rs = getUserPosts(Group_name, rs, st, user_id, TwitterUserID, false, true);
      Post_List = getCommentDetailsFromDB(rs, false, Post_List, true);


    }
    else
    {

      rs = getAllPosts(st, Group_name, true, false);
      Post_List = getresultantDataFromDB(rs, true, new ArrayList(), false);
      rs = getAllPosts(st, Group_name, false, true);
      Post_List = getCommentDetailsFromDB(rs, false, Post_List, true);
    }
    



    return AesEncryption.encrypt(Post_List.toString());
  }
  
  public ResultSet getAllPosts(Statement st, String Group_name, boolean IsTwitterData, boolean IsComment) throws SQLException
  {
    ResultSet rs = null;
    if (IsTwitterData)
    {
      if (Group_name.equals("All")) rs = st.executeQuery("select distinct name as name,TweetID as id , message as post_Detail , ImageUrl as Post_Image, Infringing_User_ID from tweetTable inner join twitter_infringingusers on Infringing_User_ID=id;"); else {
        rs = st.executeQuery("select distinct name as name,TweetID as id , message as post_Detail , ImageUrl as Post_Image, Infringing_User_ID from tweetTable inner join twitter_infringingusers on Infringing_User_ID=id where Group_Name  = '" + Group_name + "';");
      }
    } else if (IsComment)
    {
      if (Group_name.equals("All")) rs = st.executeQuery("select distinct Name as name,PostId as id, Parent_Comment_id, Comment_ID, Comment, InfringingUserId from  Comments inner join infringingusers on InfringingUserId=infringingusers.User_ID;"); else {
        rs = st.executeQuery("select distinct Name as name,PostId as id, Parent_Comment_id, Comment_ID, Comment, InfringingUserId from  Comments inner join infringingusers on InfringingUserId=infringingusers.User_ID where   Group_Name  = '" + Group_name + "';");
      }
      
    }
    else if (Group_name.equals("All")) rs = st.executeQuery("select distinct Name as name, id, post_Detail , Post_Image, Infringing_User_ID from postTable inner join infringingusers on Infringing_User_ID=infringingusers.User_ID;"); else
      rs = st.executeQuery("select distinct Name as name, id, post_Detail , Post_Image, Infringing_User_ID from postTable inner join infringingusers on Infringing_User_ID=infringingusers.User_ID where group_name ='" + Group_name + "';");
    return rs;
  }
  
  public ResultSet getUserPosts(String Group_name, ResultSet rs, Statement st, String user_id, String TwitterUserID, boolean IsTwitterData, boolean IsComment)
    throws SQLException
  {
    if (IsTwitterData)
    {
      if (Group_name.equals("All")) rs = st.executeQuery("select distinct name as name,TweetID as id , message as post_Detail , ImageUrl as Post_Image, Infringing_User_ID from tweetTable inner join twitter_infringingusers on Infringing_User_ID=id where tweetTable.User_ID = '" + TwitterUserID + "';"); else {
        rs = st.executeQuery("select distinct name as name,TweetID as id , message as post_Detail , ImageUrl as Post_Image, Infringing_User_ID from tweetTable inner join twitter_infringingusers on Infringing_User_ID=id where Group_Name  = '" + Group_name + "' and tweetTable.User_ID = '" + TwitterUserID + "';");
      }
      
    }
    else if (IsComment)
    {
      if (Group_name.equals("All")) rs = st.executeQuery("select distinct Name as name,PostId as id, Parent_Comment_id, Comment_ID, Comment, InfringingUserId from  Comments inner join infringingusers on InfringingUserId=infringingusers.User_ID where  Comments.User_ID = '" + user_id + "';"); else {
        rs = st.executeQuery("select distinct Name as name,PostId as id, Parent_Comment_id, Comment_ID, Comment, InfringingUserId from  Comments inner join infringingusers on InfringingUserId=infringingusers.User_ID where   Group_Name  = '" + Group_name + "'  and Comments.User_ID = '" + user_id + "';");
      }
      
    }
    else if (Group_name.equals("All")) rs = st.executeQuery("select distinct Name as name, id, post_Detail , Post_Image, Infringing_User_ID from postTable inner join infringingusers on Infringing_User_ID=infringingusers.User_ID where  postTable.User_ID = '" + user_id + "';"); else
      rs = st.executeQuery("select distinct Name as name, id, post_Detail , Post_Image, Infringing_User_ID from postTable inner join infringingusers on Infringing_User_ID=infringingusers.User_ID where group_name ='" + Group_name + "' and postTable.User_ID = '" + user_id + "';");
    System.out.println("IsTwitterData" + IsTwitterData + " TwitterUserID :" + TwitterUserID);
    










    return rs;
  }
  

  public List<Post> getresultantDataFromDB(ResultSet rs, boolean IsTwitterPost, List<Post> Posts, boolean IsComment)
    throws SQLException
  {
    while (rs.next())
    {

      String post_detail = rs.getString("post_Detail");
      String post_id = rs.getString("id");
      String post_image = rs.getString("Post_Image");
      String name = rs.getString("name");
      String InfringingUserId = rs.getString("Infringing_User_ID");
      Posts.add(new Post(post_detail, post_id, post_image, InfringingUserId, IsTwitterPost, IsComment, name));
      System.out.print("post_detail :" + post_detail + " IsTwitterPost :" + IsTwitterPost);
    }
    return Posts;
  }
  
  public List<Post> getCommentDetailsFromDB(ResultSet rs, boolean IsTwitterPost, List<Post> Posts, boolean IsComment)
    throws SQLException
  {
    while (rs.next())
    {
      String comment_id = rs.getString("Comment_ID");
      String Parent_Comment_id = rs.getString("Parent_Comment_id");
      String post_id = rs.getString("id") + "-" + Parent_Comment_id + "-" + comment_id;
      String Comment = rs.getString("Comment");
      String name = rs.getString("name");
      String InfringingUserId = rs.getString("InfringingUserId");
      Posts.add(new Post(Comment, post_id, "", InfringingUserId, IsTwitterPost, IsComment, name));
    }
    return Posts;
  }
}
