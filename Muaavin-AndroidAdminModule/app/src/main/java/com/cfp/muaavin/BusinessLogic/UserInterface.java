package com.cfp.muaavin.BusinessLogic;

import java.util.ArrayList;

/**
 *
 */
public interface UserInterface
{
    public ArrayList<User> getAsyncResponseUsers(ArrayList<User> users);

    public ArrayList<Post> getAsyncResponsePosts(ArrayList<Post> posts);

}
