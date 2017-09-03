package com.cfp.muaavin.facebook;

import java.util.ArrayList;

/**
 *
 */
public class PostDetail {

    public String ParentComment_ID;
    public String User_ID;
    public String infringing_user_name;
    public String infringing_user_id;
    public String coment_id;
    public String comment;
    public String post_id;
    public String post_Detail;
    public String post_image;
    public String infringing_user_profile_pic;
    public int infringing_user_index;
    public int unlike_value;
    public int group_id;
    public String PostUrl;
    public String user_state;
    public String FeedBackMessage;
    public  boolean IsTwitterPost;
    public  boolean IsComment;
    public  int count;

    public ArrayList<String> FeedBacks = new ArrayList<String>();



   public PostDetail()
    {
        ParentComment_ID = "";
        infringing_user_name = "";
        coment_id = "";
        comment = "";
        post_id = "";
        post_Detail = "";
        post_image = "";
        infringing_user_profile_pic = "";
        infringing_user_id = "";
        PostUrl = "";
        FeedBackMessage = "";
        User_ID = "";
    }

    public void setUserInformation(String UserID, String UserName , String ProfilePic , String state)
    {
        infringing_user_id = UserID;
        infringing_user_name = UserName;
        infringing_user_profile_pic = ProfilePic;
        user_state = state;

    }

    public void setPostInformation(String PostID, String PostDetail , String PostImage, String postUrl)
    {
        post_id =  PostID;
        post_Detail = PostDetail;
        post_image = PostImage;
        PostUrl = postUrl;

    }

    public void setCommentInformation(String CommentID, String Comment , String ParentCommentID, String postUrl)
    {
        coment_id =  CommentID;
        comment = Comment;
        ParentComment_ID = ParentCommentID;
    }




}
