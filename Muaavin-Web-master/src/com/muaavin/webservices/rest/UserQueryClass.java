package com.muaavin.webservices.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;















@Path("/Users") 
public class UserQueryClass {
	
	List<User> users = new ArrayList<>();
	List<User> infringing_users = new ArrayList<>();
	
	@GET
	@Path("/GetUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsers() throws Exception
	{
		infringing_users = new ArrayList<User>();
		MySqlDb db = new MySqlDb();
		Connection conn = db.connect();
		Statement st = conn.createStatement();
		ResultSet rs = null;
		String sql = "select name  , id , profilePic  ,state from users;";
		rs = (ResultSet) st.executeQuery(sql);
		getTwitterAndFacebookUsersFromDB(rs ,  false , false);
		rs = (ResultSet) st.executeQuery("select distinct id , name  , profilePic , state from twitterUsers ;");
		getTwitterAndFacebookUsersFromDB(rs ,  false , true);
		return AesEncryption.encrypt(infringing_users.toString());	
	}
	
	@GET
	@Path("/BlockUser")
	@Produces(MediaType.APPLICATION_JSON)
	public String BlockUser(@QueryParam("user_id") String user_id, @QueryParam("isTwitterUser") boolean isTwitterUser) throws Exception
	{
		user_id = AesEncryption.decrypt(user_id);
		MySqlDb Db = new MySqlDb();
		
		Connection conn = Db.connect();
		Statement st = conn.createStatement();
		String sql = "select* from BlockedUsers;";
		ResultSet rs = (ResultSet) st.executeQuery(sql);
		if(isTwitterUser)	
		{
			st.executeUpdate("UPDATE TwitterUsers SET state ='Blocked' where id = '"+user_id+"';");
			st.executeUpdate("UPDATE Twitter_InfringingUsers SET state ='Blocked' where id = '"+user_id+"';");
			return AesEncryption.encrypt("User Successfully Blocked");
		}
		else
		{
			st.executeUpdate("UPDATE infringingUsers SET state ='Blocked' where User_ID = '"+user_id+"';");
			st.executeUpdate("UPDATE Users SET state ='Blocked' where id = '"+user_id+"';");
			return AesEncryption.encrypt("User Successfully Blocked");
		}
	}
	
	// Check if user is already blocked
	public boolean isUserAlreadyBlocked(ResultSet rs, String user_id) throws SQLException
	{
		while(rs.next()) 
		{  
		   String User_id = rs.getString("User_ID");
		   if((User_id.equals(user_id ))){ return true ; }	
		}
		return false;
	}
	
	public List<User> getBlockedUsersList(ResultSet rs ,List<User> Users, boolean isTwitterData) throws SQLException // Get List of Blocked Users
	{
		users = Users;
		while(rs.next()) 
		{  		   
		   User user = User.initializeUser("", rs.getString("User_ID"), "","",isTwitterData);	   
		   users.add(user);
		}	
		//return users.toString() ;
		return users;
	}
	
	@GET
	@Path("/checkIfUserBlocked")
	@Produces(MediaType.APPLICATION_JSON)
	public String checkIfUserIsAlreadyBlocked(@QueryParam("user_id") String user_id) throws Exception
	{
		user_id = AesEncryption.decrypt(user_id);
		MySqlDb Db = new MySqlDb();
		
		Connection conn = Db.connect();
		Statement st = conn.createStatement();
		String sql = "select* from BlockedUsers;";
		ResultSet rs = (ResultSet) st.executeQuery(sql);
		if(isUserAlreadyBlocked(rs, user_id)== true)
		return AesEncryption.encrypt("User Already Blocked");
		return AesEncryption.encrypt("User not Blocked");
	}
	
	@GET
	@Path("/getBlockedUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBlockedUsers() throws Exception
	{
		
		MySqlDb Db = new MySqlDb();
		
		Connection conn = Db.connect();
		Statement st = conn.createStatement();
		ResultSet rs = (ResultSet) st.executeQuery("select* from BlockedUsers;");
		List<User> Users = getBlockedUsersList(rs,new ArrayList<User>(),false);
		rs = (ResultSet) st.executeQuery("select * from BlockedTwitterUsers;");
		Users = getBlockedUsersList(rs,Users,true);
		//String JsonResponseUsers = getBlockedUsersList(rs,new ArrayList<User>(),false).toString();
		return AesEncryption.encrypt(Users.toString());
	}
	@GET
	@Path("/UnBlockUser")
	@Produces(MediaType.APPLICATION_JSON)
	public String UnBlockUser(@QueryParam("user_id") String user_id, @QueryParam("isTwitterUser") boolean isTwitterUser) throws Exception
	{
		user_id = AesEncryption.decrypt(user_id);
		MySqlDb Db = new MySqlDb();
		Connection conn = Db.connect();
		Statement st = conn.createStatement();
		if(isTwitterUser)
		{
			st.executeUpdate("UPDATE TwitterUsers SET state ='UnBlocked' where id = '"+user_id+"';");
			st.executeUpdate("UPDATE Twitter_InfringingUsers SET state ='UnBlocked' where id = '"+user_id+"';");
			return AesEncryption.encrypt("User Unblocked");
		}
		else 
		{
			st.executeUpdate("UPDATE infringingUsers SET state ='UnBlocked' where User_ID = '"+user_id+"';");
			st.executeUpdate("UPDATE Users SET state ='UnBlocked' where id = '"+user_id+"';");
			System.out.println("User unBlocked"); return AesEncryption.encrypt("User Unblocked");
		}
	}
	////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/Highlights")
	@Produces(MediaType.APPLICATION_JSON)
	public String get_InfringingUsers(@QueryParam("name") String Group_name ,@QueryParam("user_id") String user_id ,@QueryParam("specificUserFriends") boolean specificUserFriends , @QueryParam("isTwitterData") boolean getTwitterData) throws Exception
	{
		System.out.println("getTwitterData "+getTwitterData);
		if(Group_name!=null) {Group_name = AesEncryption.decrypt(Group_name); }
		if(user_id!=null) {user_id = AesEncryption.decrypt(user_id); }
		
		MySqlDb db = new MySqlDb();
		String response = "";
		Connection conn = db.connect();
		Statement st = conn.createStatement();
		ResultSet rs = null;
		
		if(Group_name == null) { response = "No Parameters Passed.."; }
		if(specificUserFriends == false) 
		{ 
			getAllInfringingUsersFromDB(Group_name,  st, rs);
		}
		else
		{ 
			rs = getInfringingUsersFromDB(Group_name,user_id,st,rs,getTwitterData );
			getTwitterAndFacebookUsersFromDB(rs,getTwitterData,getTwitterData);
		}
		
		System.out.println("QUERY SUCCESSFULL EXECUTED");
		conn.close();
		return AesEncryption.encrypt(infringing_users.toString());
	}
	
	@GET
	@Path("/UnReportUser")
	@Produces(MediaType.APPLICATION_JSON)
	public void UnReportUsers(@QueryParam("user_id") String User_ID,@QueryParam("isTwitterData") boolean isTwitterData) throws Exception
	{
		User_ID = AesEncryption.decrypt(User_ID);
		MySqlDb db = new MySqlDb();
		Connection conn = db.connect();
		Statement st = conn.createStatement();
		deleteInfringingUserFromDB(User_ID, st, isTwitterData );
		conn.close();	
		System.out.print("isTwitterData :"+isTwitterData);
	}
	/////////// Get All InfringingUsers From DB
	public ResultSet getAllInfringingUsersFromDB(String Group_name, Statement st, ResultSet rs ) throws SQLException
	{
		
		if(Group_name.equals("All"))
		{ 
			rs = (ResultSet) st.executeQuery("select distinct id , name , profilePic , state from TwitterInfringingUsers;"); 
			getTwitterAndFacebookUsersFromDB(rs , false,true);
			rs = (ResultSet) st.executeQuery("select distinct id , name ,  Profile_pic as profilePic , state from FacebookInfringingUsers;"); 
			getTwitterAndFacebookUsersFromDB(rs , false,false);
			System.out.print("Get All infringing Users Function Group Name : All");
		}
			
		else
		{
			rs = (ResultSet) st.executeQuery("select distinct id  , name , profilePic , state from TwitterInfringingUsers where Group_Name = '"+Group_name+"';");
			getTwitterAndFacebookUsersFromDB(rs , false,true);
			rs = (ResultSet) st.executeQuery("select distinct id , name ,Profile_pic as profilePic  ,state from FacebookInfringingUsers where Group_Name = '"+Group_name+"';");
			getTwitterAndFacebookUsersFromDB(rs , false,false);
			System.out.print("Get All infringing Users Function Group Name : Specific");
		}	
		return rs;	
	}
	
	// Get Selective InfringingUsers From DB
	public ResultSet getInfringingUsersFromDB(String Group_name, String user_id ,Statement st, ResultSet rs, boolean isTwitterData ) throws SQLException
	{
		if(isTwitterData)
		{
			if(Group_name.equals("All"))
			{
				rs = (ResultSet) st.executeQuery("select distinct id, name , profilePic ,state from TwitterInfringingUsers where User_ID = '"+user_id+"';");
			}
			else
			{	
				rs = (ResultSet) st.executeQuery("select distinct id, name , profilePic, state from TwitterInfringingUsers where Group_Name = '"+Group_name+"' and User_ID = '"+user_id+"';");
			}	
		}
		else
		{
			if(Group_name.equals("All"))
			{
				rs = (ResultSet) st.executeQuery(" select distinct id, name, Profile_pic as profilePic,state from FacebookInfringingUsers where state = 'UnBlocked' and User_ID = '"+user_id+"';");
			}
			else
			{
				rs = (ResultSet) st.executeQuery(" select distinct id, name, Profile_pic as profilePic,state from FacebookInfringingUsers where state = 'UnBlocked' and User_ID = '"+user_id+"' and group_name='"+Group_name+"';");
			}	
		}
		return rs;	
	}
	// Delete InfringingUsers From DB
	public void deleteInfringingUserFromDB(String UserID , Statement st, boolean isTwitterData) throws SQLException
	{
		if(isTwitterData)
		{
			st.executeUpdate("delete from TwitterFeedBack where TweetID in (select TweetID from TweetTable where Infringing_User_ID = '"+UserID+"');");
			st.executeUpdate("delete from TwitterThumbsDown where TweetID in (select TweetID from TweetTable where Infringing_User_ID = '"+UserID+"');");
			st.executeUpdate("delete from TweetTable where Infringing_User_ID = '"+UserID+"';");
			st.executeUpdate("delete from Twitter_InfringingUsers where id  = '"+UserID+"';");
			System.out.println("User UnReported");
		}
		else
		{
			st.executeUpdate("delete from commentsThumbDown where InfringingUserId = '"+UserID+"';");
			st.executeUpdate("delete from commentFeedBack where InfriningUserId = '"+UserID+"';");
			st.executeUpdate("delete from PostTable where   InfringingUserId = '"+UserID+"';");	
			st.executeUpdate("delete from comments where   InfringingUserId = '"+UserID+"';");	
			st.executeUpdate("delete from infringingUsers where User_ID = '"+UserID+"';");
			
		}
	}
	
	
	public void getTwitterAndFacebookUsersFromDB(ResultSet rs , boolean getTwitterData , boolean twitterData) throws SQLException
	{
		while(rs.next())
		{	
			User user = User.initializeUser(rs.getString("name") ,rs.getString("id") , rs.getString("profilePic"),rs.getString("state"),twitterData);
			infringing_users.add(user);
		}
	}	
}