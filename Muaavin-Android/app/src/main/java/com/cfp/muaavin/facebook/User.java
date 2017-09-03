package com.cfp.muaavin.facebook;

import com.cfp.muaavin.twitter.TwitterUtil;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.Serializable;


public class User implements Serializable {

    public String name;
    public String id;
    public String profile_pic;
    public String profile_url;
    public static boolean user_authentication = true;
    public String state;
    public String ScreenName;
    public boolean isTwitterUser;
    public static String blockDate;
   public User()
   {


       profile_pic = "";
       profile_url = "";
       ScreenName = "";


    }

    public static User getLoggedInUserInformation() {
        User user = new User();
        Profile profile = Profile.getCurrentProfile();
        user.id = AccessToken.getCurrentAccessToken().getUserId();
        user.name = profile.getName();
        user.profile_pic = (profile.getProfilePictureUri(20, 20).toString());
        user.state = "UnBlocked";
        return user;
    }

    public static User getTwitterUserLoggedInInformation() {
        User user = new User();
        user.id =   TwitterUtil.user.id;
        user.name = TwitterUtil.user.name;
        user.state = "unBlocked";
        user.profile_pic = TwitterUtil.user.profile_pic;
        user.profile_url = TwitterUtil.user.profile_url;
        return user;

    }

    public  void setUserInformation(String user_id , String user_name, String profilePic, String profileUrl, String State) {

        id  = user_id;
        name = user_name;
        profile_pic = profilePic;
        profile_url = profileUrl;
        state = State;
    }
}
