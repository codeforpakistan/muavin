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
import android.widget.Button;
import android.widget.ListView;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.loaders.PostsLoadAsyncTask;
import com.cfp.muaavin.adapter.Posts_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.adapter.Users_CustomAdapter;
import com.cfp.muaavin.facebook.User;
import java.util.ArrayList;



public class Post_ListView extends Fragment implements AsyncResponsePosts {

    ArrayList<Post> User_Posts;
    ListView lv ;
    boolean ClipBoardOption ;
    Context context;
    boolean IsTwitterData;
    boolean GroupPostOption;
    Button loadPosts;

    public ArrayList<Post> SelectivePosts = new ArrayList<Post>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.activity_list_view, container, false);
        context = getActivity();
        SelectivePosts = (ArrayList<Post>)  getArguments().getSerializable("user_posts");
        User_Posts = new ArrayList<Post>();

        ClipBoardOption = getArguments().getBoolean("ClipBoardOption", false);
        GroupPostOption = getArguments().getBoolean("GroupPostOption", false);
        IsTwitterData =   getArguments().getBoolean("isTwitterData", false);
        loadPosts=(Button) view.findViewById(R.id.LoadPosts);
        lv=(ListView) view.findViewById(R.id.listView1);
        Posts_CustomAdapter c = new Posts_CustomAdapter(getActivity(), SelectivePosts , User.getLoggedInUserInformation().id,ClipBoardOption,GroupPostOption);
        lv.setAdapter(c);

        loadPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PostsLoadAsyncTask.nextResultsRequests != null)
                {
                    new PostsLoadAsyncTask("Timeline Posts",context, Post_ListView.this, User.getLoggedInUserInformation().id, ClipBoardOption, "me", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
                }
            }
        });
        return  view;
    }

    public void loadPosts(View view)
    {
        if (PostsLoadAsyncTask.nextResultsRequests != null)
        {
            new PostsLoadAsyncTask("Timeline Posts",context, Post_ListView.this, User.getLoggedInUserInformation().id, ClipBoardOption, "me", new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
        }
    }

    @Override
    public void getUserAndPostData(ArrayList<Post> result, String option)
    {
        SelectivePosts = FacebookUtil.Posts;
        if((!GroupPostOption)&&(!ClipBoardOption))
        SelectivePosts = Users_CustomAdapter.getSelectivePosts(FacebookUtil.ReportPostDetail.infringing_user_id, SelectivePosts);
        Posts_CustomAdapter c = new Posts_CustomAdapter( getActivity(), SelectivePosts, User.getLoggedInUserInformation().id,ClipBoardOption,GroupPostOption);
        lv.setAdapter(c);
        //((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
    }
}
