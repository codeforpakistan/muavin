package com.cfp.muaavin.BusinessLogic;


import android.content.Context;
import android.content.Intent;

import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.ui.PostListView;
import com.cfp.muaavin.ui.UsersListView;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.util.ArrayList;

public class Admin implements UserInterface{

    String  name;
    String  id;
    String  password;
    Context context;
    String  option;


    public Admin()
    {
        name = "";
        id = "";
        password = "";
    }

    public void blockUser(Context context)
    {
        option = "Block User";
        this.context = context;
        String serverURL = "http://52.176.101.55:8080/Muaavin-Web/rest/Users/GetUsers?";
        new WebHttpGetReq( option, context, this).execute(serverURL);

    }

    public void deletePost(Context context, String GroupName) throws Exception {
        option = "Delete Post";
        this.context = context;
        String serverURL = "http://52.176.101.55:8080/Muaavin-Web/rest/UsersPosts/GetUsersPosts?name="+AesEncryption.encrypt(GroupName)+"&isSpecificUserPost="+false;
        new WebHttpGetReq(option, context, this).execute(serverURL);

    }

    public void viewReportedUser(Context context) throws Exception
    {
        option ="View Reported Users";
        this.context = context;
        String serverURL = "http://52.176.101.55:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt("All")+"&specificUserFriends="+false;
        new WebHttpGetReq(option, context , this).execute(serverURL);

    }


    @Override
    public ArrayList<User> getAsyncResponseUsers(ArrayList<User> users)
    {

        Intent intent  = new Intent(context , UsersListView.class);
        intent.putExtra("users",users);
        intent.putExtra("option",option);
        context.startActivity(intent);
        return users;
    }

    @Override
    public ArrayList<Post> getAsyncResponsePosts(ArrayList<Post> posts)
    {
        Intent intent  = new Intent(context , PostListView.class);
        intent.putExtra("posts",posts);
        context.startActivity(intent);
        return posts;
    }
}
