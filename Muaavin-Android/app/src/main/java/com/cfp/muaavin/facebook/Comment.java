package com.cfp.muaavin.facebook;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class Comment  implements Serializable {

    public String parent_comment_id;
    public String comment_id;
    public String name;
    public String post_id;
    public String user_id;
    public String message;
    public String profilePic;
    public int reply_count = 0;

    public ArrayList<Comment> replies = new ArrayList<Comment>();

    public  Comment()
    {
        parent_comment_id = "";
        comment_id = "";
        user_id = "";
        name = "";
        post_id = "";
        profilePic = "";
    }

    public void setComment(String CommentID, String ParentCommentID, String Name, String PostID ,String UserID, String ProfilePic, String Message, int ReplyCount )
    {
        comment_id = CommentID;
        parent_comment_id = ParentCommentID;
        user_id = UserID;
        name = Name;
        post_id = PostID;
        message = Message;
        reply_count = ReplyCount;
        profilePic = ProfilePic;
    }
}


