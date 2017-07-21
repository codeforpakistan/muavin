package com.muaavin.webservices.rest;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONException;
import org.json.JSONObject;

@XmlRootElement
public class User {

	String Post_Id;
	int Group_Id;
	String Profile_Name;
	
	@XmlElement(name="User_ID")
	String User_ID;
	
	@XmlElement(name="User_Name")
	String User_Name;
	
	@XmlElement(name="Profile_Pic")
	String Profile_Pic;
	
	@XmlElement(name="State")
	String State;
	
	@XmlElement(name="IsTwitterUser")
	boolean IsTwitterUser;
	
	public User(){        
    }
	
	public User(String user_name, String user_Id, String profile_pic , String state,boolean isTwitteruser) {
		User_Name = user_name;
		User_ID = user_Id;
		Profile_Pic = profile_pic;
		State = state;
		IsTwitterUser = isTwitteruser;
		
    } 
	
	 @Override
	 public String toString()
	 {
	     try 
	     {	         
	        return new JSONObject().put("User_Name", User_Name).put("User_ID", User_ID).put("Profile_Pic", Profile_Pic).put("State", State).put("IsTwitterUser", IsTwitterUser).toString();
	     } 
	     catch (JSONException e) { return null; }
	     
	  }
	 
	 public static User initializeUser(String Name ,String user_id , String profile_pic, String State,boolean isTwitteruser )
	 {
		 User user = new User();
		 user.User_Name   = Name;
		 user.User_ID =  user_id;
		 user.Profile_Pic = profile_pic;
		 user.State = State;
		 user.IsTwitterUser = isTwitteruser;
		 return user;
 
	 }
	
}
