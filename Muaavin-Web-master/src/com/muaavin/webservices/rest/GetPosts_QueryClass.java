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






@Path("/Posts_Query")
public class GetPosts_QueryClass
{
  List<Post> Post_List = new ArrayList();
  String[] idArray;
  
  public GetPosts_QueryClass() {}
  
  @GET
  @Path("/GetPosts")
  @Produces({"application/json"})
  public String get_InfringingUsers(@QueryParam("name") String Group_name) throws Exception { Post_List = new ArrayList();
    Group_name = AesEncryption.decrypt(Group_name);
    MySqlDb db = new MySqlDb();
    
    String response = "";
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    ResultSet rs = null;
    
    UserQueryClass users = new UserQueryClass();
    int usersCount = users.getUsersCount();
    if (Group_name.equals("All"))
    {





      rs = st.executeQuery("select* from Group_AllCommentDetailWithFeedBack");
      Post_List = getResultantCommentData(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupAll_TweetDetailWithFeedBack");
      Post_List = getResultantTwitterPostDetailData(rs, Post_List, true, false, usersCount);


    }
    else if (Group_name.equals("A"))
    {




      rs = st.executeQuery("select* from Group_ACommentDetailWithFeedBack");
      Post_List = getResultantCommentData(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupA_TweetDetailWithFeedBack");
      Post_List = getResultantTwitterPostDetailData(rs, Post_List, true, false, usersCount);

    }
    else if (Group_name.equals("B"))
    {




      rs = st.executeQuery("select* from Group_BCommentDetailWithFeedBack");
      Post_List = getResultantCommentData(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupB_TweetDetailWithFeedBack");
      Post_List = getResultantTwitterPostDetailData(rs, Post_List, true, false, usersCount);

    }
    else if (Group_name.equals("C"))
    {




      rs = st.executeQuery("select* from Group_CCommentDetailWithFeedBack");
      Post_List = getResultantCommentData(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupC_TweetDetailWithFeedBack");
      Post_List = getResultantTwitterPostDetailData(rs, Post_List, true, false, usersCount);
    }
    System.out.println("QUERY SUCCESSFULL EXECUTED");conn.close();
    

    return AesEncryption.encrypt(Post_List.toString());
  }
  
  @GET
  @Path("/GetPostsId")
  @Produces({"application/json"})
  public String get_InfringingUsersId(@QueryParam("name") String Group_name)
    throws Exception
  {
    Post_List = new ArrayList();
    Group_name = AesEncryption.decrypt(Group_name);
    MySqlDb db = new MySqlDb();
    
    String response = "";
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    ResultSet rs = null;
    
    UserQueryClass users = new UserQueryClass();
    int usersCount = users.getUsersCount();
    if (Group_name.equals("All"))
    {





      rs = st.executeQuery("select* from Group_AllCommentDetailWithFeedBackId");
      Post_List = getResultantCommentDataId(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupAll_TweetDetailWithFeedBackId");
      Post_List = getResultantTwitterPostDetailDataId(rs, Post_List, true, false, usersCount);


    }
    else if (Group_name.equals("A"))
    {




      rs = st.executeQuery("select* from Group_ACommentDetailWithFeedBackId");
      Post_List = getResultantCommentDataId(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupA_TweetDetailWithFeedBackId");
      Post_List = getResultantTwitterPostDetailDataId(rs, Post_List, true, false, usersCount);

    }
    else if (Group_name.equals("B"))
    {




      rs = st.executeQuery("select* from Group_BCommentDetailWithFeedBackId");
      Post_List = getResultantCommentDataId(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from GroupB_TweetDetailWithFeedBackId");
      Post_List = getResultantTwitterPostDetailDataId(rs, Post_List, true, false, usersCount);

    }
    else if (Group_name.equals("C"))
    {




      rs = st.executeQuery("select* ;4from Group_CCommentDetailWithFeedBackId");
      Post_List = getResultantCommentDataId(rs, new ArrayList(), false, true, usersCount);
      rs = st.executeQuery("select* from ;GroupC_TweetDetailWithFeedBackId");
      Post_List = getResultantTwitterPostDetailDataId(rs, Post_List, true, false, usersCount);
    }
    System.out.println("QUERY SUCCESSFULL EXECUTED");conn.close();
    

    return AesEncryption.encrypt(Post_List.toString());
  }
  
  @GET
  @Path("/DeletePosts")
  @Produces({"application/json"})
  public void deletePosts(@QueryParam("Post_id") String Post_id, @QueryParam("Group_name") String group_name, @QueryParam("User_id") String user_id, @QueryParam("isPostOfSpecificUser") boolean isPostOfSpecificUser, @QueryParam("InfringingUserID") String InfringingUserID, @QueryParam("IsTwitterPost") boolean IsTwitterPost, @QueryParam("IsComment") boolean IsComment)
    throws Exception
  {
    if (Post_id != null) Post_id = AesEncryption.decrypt(Post_id);
    if (group_name != null) group_name = AesEncryption.decrypt(group_name);
    if (user_id != null) user_id = AesEncryption.decrypt(user_id);
    if (InfringingUserID != null) { InfringingUserID = AesEncryption.decrypt(InfringingUserID);
    }
    MySqlDb Db = new MySqlDb();
    
    try
    {
      Connection conn = MySqlDb.connect();
      Statement st = conn.createStatement();
      if (isPostOfSpecificUser) deleteUsersPosts(st, Post_id, user_id, InfringingUserID, IsTwitterPost, IsComment); else {
        deleteAllPosts(st, Post_id, InfringingUserID, IsTwitterPost, IsComment);
      }
    } catch (Exception e) {
      System.out.println("Inside Catch Block!");e.printStackTrace();
    }
  }
  

  public void deleteUsersPosts(Statement st, String Post_id, String user_id, String InfringingUserId, boolean IsTwitterPost, boolean IsComment)
    throws SQLException
  {
    if (IsTwitterPost)
    {
      st.executeUpdate("delete from TweetTable where TweetID = '" + Post_id + "' and  User_ID = '" + user_id + "';");
      
      st.executeUpdate("delete from Twitter_InfringingUsers where id = '" + InfringingUserId + "' and id not in (select Infringing_User_ID from tweettable);");
      st.executeUpdate("delete from TwitterThumbsDown where TweetID = '" + Post_id + "' and TweetID not in (select distinct TweetID from TweetTable);");
      st.executeUpdate("delete from TwitterFeedBack where TweetID = '" + Post_id + "' and TweetID not in (select distinct TweetID from TweetTable);");
    }
    else if (IsComment)
    {

      boolean recordPresent = false;
      idArray = Post_id.split("-");
      st.executeUpdate("delete from comments where PostId = '" + idArray[0] + "' and  User_ID ='" + user_id + "' and Comment_ID ='" + idArray[2] + "' and Parent_Comment_id = '" + idArray[1] + "';");
      st.executeUpdate("delete from infringingUsers where User_ID = '" + InfringingUserId + "' and  User_ID not in (select InfringingUserId from comments);");
      st.executeUpdate("delete from postTable where InfringingUserId = '" + InfringingUserId + "' and id = '" + idArray[0] + "' and  User_ID ='" + user_id + "' and id not in (select PostId  from comments where InfringingUserId = '" + InfringingUserId + "' and PostId = '" + idArray[0] + "' and User_ID = '" + user_id + "');");
      ResultSet rs = st.executeQuery("select* from comments where PostId = '" + idArray[0] + "' and Comment_ID ='" + idArray[2] + "' and Parent_Comment_id = '" + idArray[1] + "';");
      if (rs.next()) { recordPresent = true;
      }
      if (!recordPresent)
      {
        st.executeUpdate("delete from commentsThumbdown where post_id = '" + idArray[0] + "' and commentId ='" + idArray[2] + "' and PcommentId = '" + idArray[1] + "';");
        st.executeUpdate("delete from commentFeedBack where post_id = '" + idArray[0] + "' and Comment_id ='" + idArray[2] + "' and Pcommentid = '" + idArray[1] + "';");
      }
      System.out.print("Comment successfuly Deleted    InfringingUserId :" + InfringingUserId + " Post id :" + idArray[0] + " User id :" + user_id);
    }
    else
    {
      boolean recordPresent = false;
      st.executeUpdate("delete from postTable where id =  '" + Post_id + "' and User_ID ='" + user_id + "';");
      ResultSet rs = st.executeQuery("select* from postTable where id = '" + Post_id + "';");
      if (rs.next()) recordPresent = true;
      if (!recordPresent)
      {
        st.executeUpdate("delete from ThumbsDown where post_id =  '" + Post_id + "';");
        st.executeUpdate("delete from postFeedBack where post_id =  '" + Post_id + "' ;");
      }
      System.out.println("Post Successfully Deleted ");
    }
  }
  
  public void deleteAllPosts(Statement st, String Post_id, String InfringingUserID, boolean IsTwitterPost, boolean IsComment)
    throws SQLException
  {
    if (IsTwitterPost)
    {
      st.executeUpdate("delete from TweetTable where TweetID = '" + Post_id + "';");
      st.executeUpdate("delete from TwitterFeedBack where TweetID = '" + Post_id + "';");
      st.executeUpdate("delete from TwitterThumbsDown where TweetID = '" + Post_id + "';");
      st.executeUpdate("delete from Twitter_InfringingUsers  where id = '" + InfringingUserID + "' and id not in(select Infringing_User_ID from tweetTable);");
      st.executeUpdate("UPDATE TwitterUsers SET state ='UnBlocked' where id = '" + InfringingUserID + "' and id not in(select Infringing_User_ID from tweetTable);");
      System.out.println("Tweet successfully Deleted..." + InfringingUserID);
    }
    else if (IsComment)
    {
      idArray = Post_id.split("-");
      st.executeUpdate("delete from comments where PostId = '" + idArray[0] + "'  and Comment_ID ='" + idArray[2] + "' and Parent_Comment_id = '" + idArray[1] + "';");
      st.executeUpdate("delete from commentsThumbdown where post_id = '" + idArray[0] + "' and commentId ='" + idArray[2] + "' and PcommentId = '" + idArray[1] + "';");
      st.executeUpdate("delete from commentFeedBack where post_id = '" + idArray[0] + "' and Comment_id ='" + idArray[2] + "' and Pcommentid = '" + idArray[1] + "';");
      st.executeUpdate("delete from postTable where InfringingUserId = '" + InfringingUserID + "' and id = '" + idArray[0] + "' and infringingUserId not in (select InfringingUserId  from comments where InfringingUserId = '" + InfringingUserID + "' and PostId = '" + idArray[0] + "');");
      st.executeUpdate("delete from infringingUsers where User_ID = '" + InfringingUserID + "' and  User_ID not in (select InfringingUserId from comments);");
      

      System.out.println("Comment successfully Deleted...");
    }
    else
    {
      st.executeUpdate("delete from postTable where id =  '" + Post_id + "';");
      st.executeUpdate("delete from ThumbsDown where post_id =  '" + Post_id + "';");
      st.executeUpdate("delete from postFeedBack where post_id =  '" + Post_id + "' ;");
      System.out.println("Post successfully Deleted...");
    }
  }
  
  public List<Post> getResultantFacebookPostDetailData(ResultSet rs, List<Post> PostDetails, boolean IsTwitterPostDetail, boolean IsComment) throws SQLException
  {
    while (rs.next())
    {
      String post_id = rs.getString("id");
      String post_detail = rs.getString("post_Detail");
      



      String post_image = rs.getString("Post_Image");
      

      String FeedBackMessage = rs.getString("comment");
      int total_Unlikes = rs.getInt("total_Unlikes");
      
      PostDetails.add(new Post(post_id, post_detail, post_image, "", "", "", "", "", "", total_Unlikes, FeedBackMessage, IsTwitterPostDetail, IsComment, ""));
    }
    
    return PostDetails;
  }
  
  public List<Post> getResultantCommentData(ResultSet rs, List<Post> PostDetails, boolean IsTwitterPostDetail, boolean IsComment, int count)
    throws SQLException
  {
    while (rs.next())
    {

      String Parent_Comment_id = rs.getString("Parent_Comment_id");
      String Comment_ID = rs.getString("Comment_ID");
      String Comment = rs.getString("Comment");
      String post_detail = rs.getString("Comment");
      String FeedBackMessage = rs.getString("FeedBackMessage");
      String InfringingUserId = rs.getString("InfringingUserId");
      String InfringingUserName = rs.getString("Name");
      String InfringingUserProfilePic = rs.getString("Profile_pic");
      int total_Unlikes = rs.getInt("total_Unlikes");
      String userId = rs.getString("User_ID");
      String groupName = rs.getString("Group_Name");
      String post_id = rs.getString("id") + "-" + rs.getString("Parent_Comment_id") + "-" + rs.getString("Comment_ID");
      

      PostDetails.add(new Post(post_id, post_detail, "", Parent_Comment_id, InfringingUserId, InfringingUserName, InfringingUserProfilePic, Comment_ID, Comment, total_Unlikes, FeedBackMessage, IsTwitterPostDetail, IsComment, userId, count, groupName));
    }
    


    return PostDetails;
  }
  
  public List<Post> getResultantTwitterPostDetailData(ResultSet rs, List<Post> PostDetails, boolean IsTwitterPostDetail, boolean IsComment, int count)
    throws SQLException
  {
    while (rs.next())
    {
      String post_id = rs.getString("TweetID");
      String post_detail = rs.getString("message");
      String post_image = rs.getString("ImageUrl");
      String message = rs.getString("FeedBackMessage");
      int total_Unlikes = rs.getInt("total_Unlikes");
      String InfringingUserId = rs.getString("id");
      String InfringingUserName = rs.getString("name");
      String InfringingUserProfilePic = rs.getString("profilePic");
      String userId = rs.getString("User_ID");
      String groupName = rs.getString("Group_Name");
      

      PostDetails.add(new Post(post_id, post_detail, post_image, "", InfringingUserId, InfringingUserName, InfringingUserProfilePic, "", "", total_Unlikes, message, IsTwitterPostDetail, IsComment, userId, count, groupName));
    }
    


    return PostDetails;
  }
  
  public List<Post> getResultantCommentDataId(ResultSet rs, List<Post> PostDetails, boolean IsTwitterPostDetail, boolean IsComment, int count)
    throws SQLException
  {
    while (rs.next())
    {

      String Parent_Comment_id = rs.getString("Parent_Comment_id");
      String Comment_ID = rs.getString("Comment_ID");
      String Comment = rs.getString("Comment");
      String post_detail = rs.getString("Comment");
      String FeedBackMessage = rs.getString("FeedBackMessage");
      int FeedBackId = rs.getInt("fid");
      String InfringingUserId = rs.getString("InfringingUserId");
      String InfringingUserName = rs.getString("Name");
      String InfringingUserProfilePic = rs.getString("Profile_pic");
      int total_Unlikes = rs.getInt("total_Unlikes");
      String userId = rs.getString("User_ID");
      String post_id = rs.getString("id") + "-" + rs.getString("Parent_Comment_id") + "-" + rs.getString("Comment_ID");
      String groupName = rs.getString("Group_Name");
      

      PostDetails.add(new Post(post_id, post_detail, "", Parent_Comment_id, InfringingUserId, InfringingUserName, InfringingUserProfilePic, Comment_ID, Comment, total_Unlikes, FeedBackMessage, IsTwitterPostDetail, IsComment, userId, count, FeedBackId, groupName));
    }
    


    return PostDetails;
  }
  
  public List<Post> getResultantTwitterPostDetailDataId(ResultSet rs, List<Post> PostDetails, boolean IsTwitterPostDetail, boolean IsComment, int count)
    throws SQLException
  {
    while (rs.next())
    {
      String post_id = rs.getString("TweetID");
      String post_detail = rs.getString("message");
      String post_image = rs.getString("ImageUrl");
      String message = rs.getString("FeedBackMessage");
      int FeedBackId = rs.getInt("fid");
      int total_Unlikes = rs.getInt("total_Unlikes");
      String InfringingUserId = rs.getString("id");
      String InfringingUserName = rs.getString("name");
      String InfringingUserProfilePic = rs.getString("profilePic");
      String userId = rs.getString("User_ID");
      String groupName = rs.getString("Group_Name");
      

      PostDetails.add(new Post(post_id, post_detail, post_image, "", InfringingUserId, InfringingUserName, InfringingUserProfilePic, "", "", total_Unlikes, message, IsTwitterPostDetail, IsComment, userId, count, FeedBackId, groupName));
    }
    


    return PostDetails;
  }
}
