package com.cfp.muaavin.loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.cfp.muaavin.facebook.Group;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tooba Saeed on 27/02/2017.
 */

public class GroupsLoadAsyncTask extends AsyncTask<ArrayList<Post> , Void, ArrayList<String>> {



    private ProgressDialog dialog;
    public Context context;
    public  ArrayList<Group> Groups = new ArrayList<Group>();
    public GroupsResponse GroupResponseDelegate;
    public String GroupName, Error;

    public  interface GroupsResponse
    {
        public void getGroups(ArrayList<Group> Groups);
    }

    public  GroupsLoadAsyncTask(String groupName, Context contex, GroupsResponse GroupResponse)
    {
        context = contex;
        GroupName = groupName;
        GroupResponseDelegate = GroupResponse;
        Error = "";
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Data, Please wait...");
        dialog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<Post>... arrayLists)
    {
        getGroups(); return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {

        if (dialog.isShowing()) { dialog.dismiss(); }

        if(!Error.equals("")) { Toast.makeText(context, Error, Toast.LENGTH_LONG).show();}
        else { GroupResponseDelegate.getGroups(Groups); }
    }

    public ArrayList<Group> getJsonGroupNames(GraphResponse response)
    {
        JSONObject content = response.getJSONObject();
        JSONArray data = content.optJSONArray("data");
        for(int i = 0; i < data.length(); i++)
        {
            Group group = new Group();
            JSONObject GroupObject = data.optJSONObject(i);
            group.GroupName = GroupObject.optString("name");
            group.GroupId = GroupObject.optString("id");
            Groups.add(group);
        }
       return  Groups;
    }

    public void getGroups()
    {
        final String[] afterString = {""};  // will contain the next page cursor
        final Boolean[] noData = {false};   // stop when there is no after cursor

        do {
            Bundle params = new Bundle();
            params.putString("after", afterString[0]);
            params.putString("q",GroupName);
            params.putString("type","group");
            GraphRequest gr = new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/search",
            params,
            HttpMethod.GET,
            new GraphRequest.Callback()
            {
                public void onCompleted(GraphResponse response)
                {
                    if (response.getError() == null)
                    {
                        JSONObject jsonObject = response.getJSONObject();
                        Groups = getJsonGroupNames(response);
                        if (!jsonObject.isNull("paging"))
                        {
                            JSONObject paging = jsonObject.optJSONObject("paging");
                            JSONObject cursors = paging.optJSONObject("cursors");
                            if (!cursors.isNull("after")) { afterString[0] = cursors.optString("after"); }
                            else { noData[0] = true; }
                        }
                        else { noData[0] = true; }
                    }
                    else { noData[0] = true; Error = "Ether Network Error or Invalid Input"; }
                }
            });
            gr.executeAndWait();

        }while(noData[0] != true);
    }


}
