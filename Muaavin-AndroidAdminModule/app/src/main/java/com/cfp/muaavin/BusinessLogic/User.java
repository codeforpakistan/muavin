package com.cfp.muaavin.BusinessLogic;

import java.io.Serializable;

/**
 *
 */
public class User implements Serializable {

    public String name;
    public String id;
    public String profilePic;
    public String profileUrl;
    public String state;
    public boolean isTwitterUser;

    public User()
    {
        name = "";
        id = "";
        profileUrl= "";
        profilePic = "";

    }

}
