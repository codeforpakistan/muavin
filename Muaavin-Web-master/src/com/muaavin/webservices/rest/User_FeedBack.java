package com.muaavin.webservices.rest;

import java.sql.Connection;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;



@Path("/FeedBack") 
public class User_FeedBack {
	
	String[] idArray;

	@GET
	@Path("/Add_FeedBack")
	@Produces(MediaType.APPLICATION_JSON)
	public String Add_FeedBack(@QueryParam("user_id") String user_id, @QueryParam("InfringingUserId") String InfringingUserId,@QueryParam("post_id") String post_id,@QueryParam("comment") String comment ,@QueryParam("IsTwitterFeedBack") boolean IsTwitterFeedBack ,@QueryParam("IsComment") boolean IsComment) throws Exception
	{
		user_id = AesEncryption.decrypt(user_id); post_id = AesEncryption.decrypt(post_id); comment = AesEncryption.decrypt(comment);
		if(InfringingUserId!= null) { InfringingUserId = AesEncryption.decrypt(InfringingUserId); }
		idArray = post_id.split("-");
		
		MySqlDb Db = new MySqlDb();
		Connection conn = Db.connect();
		Statement st = conn.createStatement();
		if(IsTwitterFeedBack) { st.executeUpdate("INSERT INTO twitterfeedback(user_id,  TweetID, Message) VALUES ('"+user_id+"','"+post_id+"','"+comment+"');"); }
		
		else if(IsComment){ st.executeUpdate("INSERT INTO commentFeedBack(post_id,   Pcommentid, Comment_id ,user_id, InfriningUserId, comment) VALUES('"+idArray[0]+"','"+idArray[1]+"','"+idArray[2]+"','"+user_id+"','"+InfringingUserId+"','"+comment+"');"); }
		
		else { st.executeUpdate("INSERT INTO PostFeedBack(user_id,  post_id, comment) VALUES ('"+user_id+"','"+post_id+"','"+comment+"');"); }
		
		return AesEncryption.encrypt(comment);
		
	}
}