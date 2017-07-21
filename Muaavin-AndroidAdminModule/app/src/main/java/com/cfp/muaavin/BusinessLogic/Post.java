package com.cfp.muaavin.BusinessLogic;

import java.io.Serializable;

/**
 *
 */
public class Post implements Serializable {

 public String id;
 public String message;
 public String image;
 public String postUrl;
 public boolean IsTwitterPost;
 public boolean IsComment;
 public User PostOwner = new User();


    public Post()
  {
    id = "";
    message = "";
    image = "";
    postUrl = "";

  }


}
