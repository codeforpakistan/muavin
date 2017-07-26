package com.cfp.muaavin.facebook;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public interface AsyncResponsePosts {

    public void getUserAndPostData(ArrayList<Post> results, String option);
    public void  postLink(String type, String postLink, String userProfile, String message, String userName);
}
