package com.cfp.muaavin.BusinessLogic;

import com.cfp.muaavin.helper.PostDetail;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public interface UserInterface
{
    public ArrayList<User> getAsyncResponseUsers(ArrayList<User> users);

    public ArrayList<Post> getAsyncResponsePosts(ArrayList<Post> posts);


}
