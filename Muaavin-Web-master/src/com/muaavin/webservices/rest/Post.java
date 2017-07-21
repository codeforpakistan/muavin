package com.muaavin.webservices.rest;

import javax.xml.bind.annotation.XmlElement;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

	public String post_id;
	public String post_detail;
	Boolean isOnlyPostDetail;
	
	
	@XmlElement(name="Parent_CommentID")
	String Parent_CommentID;
	
	@XmlElement(name="infringingUserId")
	String infringingUserId;
	
	@XmlElement(name="infringingUser_name")
	String infringingUser_name;
	
	@XmlElement(name="infringingUser_ProfilePic")
	String infringingUser_ProfilePic;
	
	@XmlElement(name="CommentID")
	String CommentID;
	
	@XmlElement(name="Comment")
	String Comment;
	
	@XmlElement(name="Post_ID")
	String Post_ID;
	
	@XmlElement(name="User_ID")
	String User_ID;

	@XmlElement(name="Post_Detail")
	String Post_Detail;
	
	@XmlElement(name="Post_Image")
	String Post_Image;
	
	@XmlElement(name="unlike_value")
	int unlike_value;
	
	@XmlElement(name="FeedBackMessage")
	String FeedBackMessage;
	
	@XmlElement(name="IsTwitterPost")
	boolean IsTwitterPost;
	
	@XmlElement(name="IsComment")
	boolean IsComment;

	

	
	public Post(String post_id, String post_detail,String Post_Image,String Parent_CommentID,String InfringingUserId,String infringingUser_name,String infringUser_ProfilePic,String CommentID,String Comment , int unlike_value , String feedBackMessage,boolean isTwitterPost,boolean isComment, String userId) {
		Post_ID = post_id;
		Post_Detail = post_detail;
		this.Post_Image = Post_Image;
		this.Parent_CommentID = Parent_CommentID;
		this.infringingUser_name = infringingUser_name;
		this.infringingUser_ProfilePic = infringUser_ProfilePic;
		this.CommentID = CommentID;
		this.Comment = Comment;
		this.unlike_value = unlike_value;
		FeedBackMessage = feedBackMessage;
		IsTwitterPost = isTwitterPost;
		IsComment = isComment;
		infringingUserId = InfringingUserId;
		//this.infringingUser_name = n;
		isOnlyPostDetail = false;
		this.User_ID = userId;
    } 
	
	public Post( String post_detail , String post_id, String post_image,String InfringingUserId, boolean isTwitterPost, boolean isComment,String n) {
		
		Post_Detail = post_detail;	
		Post_ID = post_id;
		isOnlyPostDetail = true;
		Post_Image = post_image;
		IsTwitterPost = isTwitterPost;
		IsComment = isComment;
		infringingUserId = InfringingUserId;		
		this.infringingUser_name = n;
		
    } 
	
	 @Override
	    public String toString()
	 	{
	        try 
	        {
	            // takes advantage of toString() implementation to format {"a":"b"}
	        	JSONObject jobj;
	        	if(isOnlyPostDetail == false){
	        		
	        	jobj = new JSONObject().put("Post_ID", Post_ID);
	        	jobj.put("Post_Detail", Post_Detail);
	        	jobj.put("Parent_CommentID", Parent_CommentID);
	        	jobj.put("infringingUser_name", infringingUser_name);
	        	jobj.put("CommentID", CommentID);
	        	jobj.put("Comment", Comment);
	        	jobj.put("Post_Image", Post_Image); //Post_Image
	        	jobj.put("infringingUser_ProfilePic", infringingUser_ProfilePic);
	        	jobj.put("unlike_value", unlike_value);
	        	jobj.put("FeedBackMessage", FeedBackMessage);
	        	jobj.put("infringingUserId", infringingUserId);
	        	jobj.put("IsTwitterPost", IsTwitterPost);
	        	jobj.put("IsComment", IsComment);
	        	jobj.put("User_ID", User_ID);

	        	//jobj.put("infringingUser_name", infringingUser_name);
	        	}
	        	
	        	else
	        	{
	        		 jobj = new JSONObject().put("Post_Detail", Post_Detail);
	        		 jobj.put("Post_ID", Post_ID);
	        		 jobj.put("Post_Image", Post_Image);
	        		 jobj.put("infringingUserId", infringingUserId);
	        		 jobj.put("IsTwitterPost", IsTwitterPost);
	        		 jobj.put("IsComment", IsComment);
	        		 jobj.put("infringingUser_name", infringingUser_name);
	 	        	}

	        	
	        return  jobj.toString();//JSONObject().put("Post_ID", Post_ID).put("Post_Detail", Post_Detail).toString();
	        } 
	        catch (JSONException e) {  return null; }
  
	 	}
	 
	 
	
}