package com.cfp.muaavin.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.cfp.muaavin.adapter.BrowsePostCustomAdapter;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.web.WebHttpGetReq;
import java.util.ArrayList;
import static com.cfp.muaavin.ui.TwitterLoginActivity.session;


public class BrowsePost_ListView extends Fragment implements AsyncResponsePosts ,BrowsePostCustomAdapter.uIUpdate{

    Context context;
    String Group_name;
    String user_id , TwitterUserId;
    ArrayList<Post> Posts ;
    ListView browsePost_Listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view  = inflater.inflate(R.layout.browse_post_layout, container, false);
        context = getActivity();
        browsePost_Listview = (ListView)view.findViewById(R.id.BrowsePost_Listview);

        Group_name = getArguments().getString("Group_name");
        user_id =  getArguments().getString("user_id");
        String serverURL = null;
        try {
            if(session==null) { TwitterUserId = "";  } else { TwitterUserId = String.valueOf(session.getUserId()); }
//            serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/UsersPosts/GetUsersPosts?name="+ AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(User.getLoggedInUserInformation().id)+"&isSpecificUserPost="+true+"&TwitterUserID="+AesEncryption.encrypt(TwitterUserId);
            serverURL = "http://52.176.101.55:8080/Muaavin-Web/rest/UsersPosts/GetUsersPosts?name="+ AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(User.getLoggedInUserInformation().id)+"&isSpecificUserPost="+true+"&TwitterUserID="+AesEncryption.encrypt(TwitterUserId);
        } catch (Exception e) { e.printStackTrace(); }

        new WebHttpGetReq(context,getActivity(), 4,this,null).execute(serverURL);

        return view;
    }



    @Override
    public void getUserAndPostData(ArrayList<Post> result, String option) {

        Posts = result;
        BrowsePostCustomAdapter c = new BrowsePostCustomAdapter(getActivity(),this, result, Group_name , User.getLoggedInUserInformation().id,browsePost_Listview);
        browsePost_Listview.setAdapter(c);
    }


    @Override
    public void removeItem(int position) {
        Posts.remove(position);
        ((BaseAdapter) browsePost_Listview.getAdapter()).notifyDataSetChanged();
    }

 /*   private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
        }

        @Override
        protected void onPostExecute(Void result) {



        }

    }*/

}
