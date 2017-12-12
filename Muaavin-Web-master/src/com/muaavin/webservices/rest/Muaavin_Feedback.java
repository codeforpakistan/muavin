package com.muaavin.webservices.rest;

import java.sql.Connection;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;



@Path("/muaavinFeedBack")
public class Muaavin_Feedback
{
  public Muaavin_Feedback() {}
  
  @GET
  @Path("/Add_MuaavinFeedBack")
  @Produces({"application/json"})
  public String User_FeedBack(@QueryParam("user_id") String user_id, @QueryParam("rating") String rating, @QueryParam("comments") String comments)
    throws Exception
  {
    user_id = AesEncryption.decrypt(user_id);rating = AesEncryption.decrypt(rating);comments = AesEncryption.decrypt(comments);
    
    MySqlDb Db = new MySqlDb();
    Connection conn = MySqlDb.connect();
    Statement st = conn.createStatement();
    st.executeUpdate("INSERT INTO muaavin_feedback(user_id,  rating, comments) VALUES ('" + user_id + "','" + rating + "','" + comments + "');");
    
    return AesEncryption.encrypt(comments);
  }
}
