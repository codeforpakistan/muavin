package com.cfp.muaavin.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.loaders.PostsLoadAsyncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.adapter.Users_CustomAdapter;
import com.cfp.muaavin.facebook.User;
import java.util.ArrayList;


public class Users_ListView extends Fragment implements AsyncResponsePosts{

    ListView UserListView ;
    Context context;
    boolean isClipboardData, isTwitterData;
    Button LoadButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view  = inflater.inflate(R.layout.users, container, false);
        context = getActivity();
        UserListView = (ListView) view.findViewById(R.id.listView2);
        LoadButton = (Button)  view.findViewById(R.id.LoadButton);
        isTwitterData = getArguments().getBoolean("isTwitterData");

        Users_CustomAdapter c = new Users_CustomAdapter( context, getActivity(),FacebookUtil.Posts,FacebookUtil.users,isTwitterData);
        UserListView.setAdapter(c);

        LoadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isClipboardData= false; FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
            if (PostsLoadAsyncTask.nextResultsRequests != null)
                new PostsLoadAsyncTask("ReportUsers",context, Users_ListView.this, User.getLoggedInUserInformation().id, isClipboardData, "me", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
        }});

        return view;
    }

    @Override // Get Facebook Posts and Users
    public void getUserAndPostData(ArrayList<Post> result,String option) {
        ((BaseAdapter) UserListView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void postLink(String type, String postLink, String userProfile, String message, String userName, int check) {

    }

    public void LoadUsers(View view)
    {
        isClipboardData= false; FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
        if (PostsLoadAsyncTask.nextResultsRequests != null)
        new PostsLoadAsyncTask("ReportUsers",context, Users_ListView.this, User.getLoggedInUserInformation().id, isClipboardData, "me", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

}
