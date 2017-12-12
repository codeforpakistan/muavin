package com.muaavin.webservices.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONException;
import org.json.JSONObject;






@XmlRootElement
public class InfringingUser
{
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
  
  public InfringingUser() {}
  
  public InfringingUser(String user_name, String user_Id, String profile_pic, String state)
  {
    User_Name = user_name;
    User_ID = user_Id;
    Profile_Pic = profile_pic;
    State = state;
  }
  

  public String toString()
  {
    try
    {
      return new JSONObject().put("User_Name", User_Name).put("User_ID", User_ID).put("Profile_Pic", Profile_Pic).put("State", State).toString();
    }
    catch (JSONException e) {}
    
    return null;
  }
  

  public static InfringingUser initializeUser(String Name, String user_id, String profile_pic, String State)
  {
    InfringingUser user = new InfringingUser();
    user.User_Name = Name;
    user.User_ID = user_id;
    user.Profile_Pic = profile_pic;
    State = State;
    return user;
  }
}
