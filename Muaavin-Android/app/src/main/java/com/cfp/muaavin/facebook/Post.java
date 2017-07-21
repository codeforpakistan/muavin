package com.cfp.muaavin.facebook;

import java.io.Serializable;
import java.util.ArrayList;


public class Post implements Serializable {

   public User PostOwner = new User();
   public  String id ;
   public  String message;
   public  String image;
   public String post_url;
   public int comment_count = 0;
   public ArrayList<Comment> Comments = new ArrayList<Comment>();
   public boolean IsTwitterPost;
   public boolean IsComment;

   public Post()
   {

      image = "";
      message = "";
      post_url = "";

   }

   public Post setPost(String id ,String message ,String image , String post_url, int comment_count  )
   {
      Post post = new Post();
      post. id = id;
      post. message = message;
      post. image =image;
      post.post_url = post_url;
      post.comment_count = comment_count;
      return post;
   }
}

