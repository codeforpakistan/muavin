package com.muaavin.webservices.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;



@Path("/ThumbsDown") 
public class ThumbsDown {
	
	ResultSet rs;
	String[] idArray;
	String response;

	@GET
	@Path("/Add_ThumbsDown")
	@Produces(MediaType.APPLICATION_JSON)
	public String Add_ThumbsDown(@QueryParam("user_id") String user_id, @QueryParam("post_id") String post_id, @QueryParam("InfringingUserId") String InfringingUserId, @QueryParam("IsTwitterPost") boolean IsTwitterPost, @QueryParam("IsComment") boolean IsComment ) throws Exception
	{
		user_id = AesEncryption.decrypt(user_id);
		post_id = AesEncryption.decrypt(post_id);
		if(InfringingUserId!= null) { InfringingUserId = AesEncryption.decrypt(InfringingUserId); }
		idArray = post_id.split("-");
		
		MySqlDb Db = new MySqlDb();
		Connection conn = Db.connect();
		Statement st = conn.createStatement();
		boolean RecordPresent = false;
		
		if(IsTwitterPost) { rs = (ResultSet) st.executeQuery("select TweetID as post_id, User_ID as user_id from TwitterThumbsDown where  TweetID = '"+post_id+"' and User_ID ='"+user_id+"';"); }
		
		else if(IsComment){  rs = (ResultSet) st.executeQuery("select post_id,  commentId, PcommentId, user_id  from commentsThumbdown where post_id = '"+idArray[0]+"' and PcommentId = '"+idArray[1]+"' and commentId = '"+idArray[2]+"' and user_id = '"+user_id+"';"); }
		
		else { rs = (ResultSet) st.executeQuery("select post_id, user_id from thumbsDown where  post_id = '"+post_id+"' and User_ID ='"+user_id+"';"); }
		
		if(RecordPresent = checkIfRecordAlreadyPresent(rs, post_id, user_id) == true)
		{
			if(IsTwitterPost) 
			{ 
				st.executeUpdate("delete from TwitterThumbsDown where User_ID  = '"+user_id+"' and TweetID = '"+post_id+"';");
			}
			
			else if(IsComment)
			{ 
				st.executeUpdate("delete  from commentsThumbdown where post_id = '"+idArray[0]+"' and PcommentId = '"+idArray[1]+"' and commentId = '"+idArray[2]+"' and user_id = '"+user_id+"';"); 
			}
			
			else 
			{ 
				st.executeUpdate("delete from thumbsDown where user_id = '"+user_id+"' and post_id = '"+post_id+"';");
			} 
			response = "Record Deleted";
		}
		else
		{
			if(IsTwitterPost) { st.executeUpdate("INSERT INTO TwitterThumbsDown(User_ID,  TweetID) VALUES('"+user_id+"','"+post_id+"');"); }
			
			else if(IsComment) { st.executeUpdate("INSERT INTO commentsThumbDown(post_id,  PcommentId, commentId ,user_id, InfringingUserId) VALUES('"+idArray[0]+"','"+idArray[1]+"','"+idArray[2]+"','"+user_id+"','"+InfringingUserId+"');"); }
			
			else { st.executeUpdate("INSERT INTO ThumbsDown(user_id,  post_id) VALUES('"+user_id+"','"+post_id+"');"); }
			
			System.out.println("User id"+ user_id +"Post id" +post_id);
			response = "Record Added";
		}
		return AesEncryption.encrypt(response);
		
	}
	
	public boolean checkIfRecordAlreadyPresent(ResultSet rs , String post_id, String user_id) throws SQLException
	{
		boolean RecordPresent = false;
		while(rs.next()) { RecordPresent = true; }
		return RecordPresent;
	}
		
}
