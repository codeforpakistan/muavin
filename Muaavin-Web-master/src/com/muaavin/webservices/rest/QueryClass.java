package com.muaavin.webservices.rest;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;






@Path("/Query")
public class QueryClass
{
  List<InfringingUser> infringing_users = new ArrayList();
  
  public QueryClass() {}
  
  @POST
  @Path("/Highlights")
  @Produces({"application/json"})
  public String get_InfringingUsers(@QueryParam("name") String Group_name, @QueryParam("user_id") String user_id, @QueryParam("specificUserFriends") boolean specificUserFriends) throws Exception { if (Group_name != null) Group_name = AesEncryption.decrypt(Group_name);
    if (user_id != null) { user_id = AesEncryption.decrypt(user_id);
    }
    MySqlDb db = new MySqlDb();
    String response = "";
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    ResultSet rs = null;
    
    if (Group_name == null) response = "No Parameters Passed..";
    if (!specificUserFriends) rs = getAllInfringingUsersFromDB(Group_name, st, rs); else {
      rs = getInfringingUsersFromDB(Group_name, user_id, st, rs);
    }
    
    while (rs.next())
    {
      InfringingUser user = InfringingUser.initializeUser(rs.getString("Name"), rs.getString("User_ID"), rs.getString("Profile_pic"), rs.getString("state"));
      infringing_users.add(user);
    }
    System.out.println("QUERY SUCCESSFULL EXECUTED");
    conn.close();
    return AesEncryption.encrypt(infringing_users.toString());
  }
  
  public ResultSet getAllInfringingUsersFromDB(String Group_name, Statement st, ResultSet rs)
    throws SQLException
  {
    if (Group_name.equals("All")) {
      rs = st.executeQuery("select distinct(infringingUsers.User_ID) ,infringingUsers.Name, infringingUsers.Profile_pic ,infringingUsers.state from groupTable,infringingUsers where groupTable.id=infringingUsers.Group_ID ;");
    }
    else {
      rs = st.executeQuery("select distinct(infringingUsers.User_ID), infringingUsers.Name,  infringingUsers.Profile_pic, infringingUsers.state  from groupTable,infringingUsers where groupTable.id=infringingUsers.Group_ID and groupTable.name='" + Group_name + "';");
    }
    return rs;
  }
  
  public ResultSet getInfringingUsersFromDB(String Group_name, String user_id, Statement st, ResultSet rs) throws SQLException
  {
    if (Group_name.equals("All")) {
      rs = st.executeQuery("select distinct(infringingUsers.User_ID) ,infringingUsers.Name, infringingUsers.Profile_pic ,infringingUsers.state from groupTable,infringingUsers where groupTable.id=infringingUsers.Group_ID and infringingUsers.state = 'UnBlocked' and infringingUsers.Profile_Name = '" + user_id + "';");
    }
    else {
      rs = st.executeQuery("select distinct(infringingUsers.User_ID), infringingUsers.Name,  infringingUsers.Profile_pic, infringingUsers.state  from groupTable,infringingUsers where groupTable.id=infringingUsers.Group_ID and infringingUsers.state = 'UnBlocked' and infringingUsers.Profile_Name ='" + user_id + "' and groupTable.name='" + Group_name + "';");
    }
    return rs;
  }
  
  @POST
  @Path("/UnReportUser")
  @Produces({"application/json"})
  public void UnReportUsers(@QueryParam("user_id") String User_ID) throws Exception
  {
    User_ID = AesEncryption.decrypt(User_ID);
    MySqlDb db = new MySqlDb();
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    deleteInfringingUserFromDB(User_ID, st);
    conn.close();
  }
  
  public void deleteInfringingUserFromDB(String UserID, Statement st)
    throws SQLException
  {
    st.executeUpdate("delete from postTable where id in (select Post_ID from infringingUsers where Post_ID in (select DISTINCT(Post_ID) from infringingUsers where User_ID = '" + UserID + "') group by Post_ID having COUNT(DISTINCT User_ID) = 1);");
    st.executeUpdate("delete from posts_comments_table where Post_ID in (select Post_ID from infringingUsers where Post_ID in (select DISTINCT(Post_ID) from infringingUsers where User_ID = '" + UserID + "') group by Post_ID having COUNT(DISTINCT User_ID) = 1);");
    st.executeUpdate("delete from infringingUsers where User_ID = '" + UserID + "';");
  }
}
