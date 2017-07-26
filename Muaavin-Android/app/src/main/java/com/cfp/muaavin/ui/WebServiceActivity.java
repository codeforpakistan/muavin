package com.cfp.muaavin.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.TextView;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.loaders.PostsLoadAsyncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.adapter.Higlights_CustomAdapter;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.facebook.User;
import java.util.ArrayList;
import static com.cfp.muaavin.web.DialogBox.CategoryName;
import static com.facebook.FacebookSdk.getApplicationContext;


public class WebServiceActivity extends Fragment implements UserInterface , AsyncResponsePosts, UiUpdate {

    public Context context;
    TextView uiUpdate;
    ArrayList<User> infringing_friends;
    ListView InfringingUserListView;
    String Group_name;
    ArrayList<String> InfringingUserIds;
    Button loadUsers;
    String DataType; View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view  = inflater.inflate(R.layout.activity_web_service, container, false);
        context = getActivity();
        initializeUiElements();
        Group_name = CategoryName;
        loadUsers = (Button)view.findViewById(R.id.LoadButton);

        infringing_friends = (ArrayList<User>) getArguments().getSerializable("InfringingUsers");
        InfringingUserIds =  (ArrayList<String>) getArguments().getSerializable("InfringingUsersIds");

        DataType = getArguments().getString("DataType");

        if(infringing_friends.size() == 0) { uiUpdate.setText(" Currently No record found"); }
        else { uiUpdate.setText("Group :" + Group_name); }
        Higlights_CustomAdapter c = new Higlights_CustomAdapter(getActivity(), infringing_friends);
        InfringingUserListView.setAdapter(c);

        loadUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(DataType.equals("Facebook"))
                    if (PostsLoadAsyncTask.nextResultsRequests != null)
                        new PostsLoadAsyncTask("Facebook Users",context, WebServiceActivity.this, "", false, "me", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
            }
        });
        return view;
    }

    public  void LoadUsers(View view)
    {
        if(DataType.equals("Facebook"))
        if (PostsLoadAsyncTask.nextResultsRequests != null)
        new PostsLoadAsyncTask("Facebook Users",context, WebServiceActivity.this, "", false, "me", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

    @Override
    public void getUserAndPostData(ArrayList<Post> results, String option) {
        getReportedFriends(InfringingUserIds, DataType); }

    @Override
    public void postLink(String type, String postLink, String userProfile, String message, String userName) {

    }

    @Override
    public void getReportedFriends(ArrayList<String> InfringingUserIds, String dataType) {

        ArrayList<User> infringing_friends = new ArrayList<User>();
        ArrayList<User> friends = FacebookLoginActivity.friend_list;
        if(dataType.equals("Twitter")) { friends = TwitterUtil.Followers ;}

        for (int j = 0; j < friends.size(); j++)
        {
            String friend_id = friends.get(j).id;
            if (InfringingUserIds.contains(friend_id)) {  infringing_friends.add(friends.get(j)); }
        }
        updateUi(infringing_friends,InfringingUserIds,dataType);
    }

    @Override
    public void getBlockedUsers(ArrayList<String> FacebookUserIds , ArrayList<String> TwitterUserIds) {

    }

    public void initializeUiElements()
    {
        uiUpdate =   (TextView) view.findViewById(R.id.output);
        InfringingUserListView = (ListView) view.findViewById(R.id.listView1);
    }

    @Override
    public void updateUi(ArrayList<User> infringing_friends, ArrayList<String> infringingUserIds,String dataType )
    {
        if(infringing_friends.size() > 0)
        {
            uiUpdate.setText("Group :"+Group_name);
            Higlights_CustomAdapter c = new Higlights_CustomAdapter(getActivity(), infringing_friends);
            InfringingUserListView.setAdapter(c);
            //((BaseAdapter) InfringingUserListView.getAdapter()).notifyDataSetChanged();
        }
    }
}

