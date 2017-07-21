package com.cfp.muaavin.loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 *
 */
public class FriendsLoadAsyncTask extends AsyncTask<ArrayList<User> , Void, ArrayList<User>> {

    private ProgressDialog dialog;
    public Context context;
    public static ArrayList<User> users;

    public FriendsLoadAsyncTask(Context contex)
    {
        context = contex;

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Friends Data, Please Wait..");
        dialog.show();
        super.onPreExecute();

    }
    @Override
    protected ArrayList<User> doInBackground(ArrayList<User>... params) {

        getFriends();
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> result) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

   /* public void getFriends()
    {

         Bundle params = new Bundle();
         params.putString("limit","900");
         GraphRequest gr =  new GraphRequest(
         AccessToken.getCurrentAccessToken(),
        "/"+User.getLoggedInUserInformation().id+"/friends",
         params,
         HttpMethod.GET,
         new GraphRequest.Callback() {
         public void onCompleted(GraphResponse response) {
         // handle the result
         if(response.getError()==null)
         getJsonFriends(response);
         }});
         gr.executeAndWait();

    }*/


    public ArrayList<User> getJsonFriends(GraphResponse response)
    {
        ArrayList<User> users = new ArrayList<User>();
        JSONArray FriendsData = response.getJSONObject().optJSONArray("data");

        for(int i = 0 ; i < FriendsData.length() ; i++)
        {
            User user = new User();
            JSONObject friend = FriendsData.optJSONObject(i);
            user.setUserInformation(friend.optString("id"),friend.optString("name"),"","https://www.facebook.com/" + friend.optString("id"),"");
            users.add(user);
        }
        FacebookLoginActivity.friend_list = users;
        return users;
    }

    public void getFriends()
    {
        final String[] afterString = {""};  // will contain the next page cursor
        final Boolean[] noData = {false};   // stop when there is no after cursor

        do {
            Bundle params = new Bundle();
            params.putString("after", afterString[0]);
            params.putString("field","friends{name,id,picture}");
            GraphRequest gr = new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/"+ Profile.getCurrentProfile().getId()+"/friends",
            params,
            HttpMethod.GET,
            new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
            if (response.getError() == null) {
            JSONObject jsonObject = response.getJSONObject();
            getJsonFriends(response);

            if (!jsonObject.isNull("paging")) {
            JSONObject paging = jsonObject.optJSONObject("paging");
            JSONObject cursors = paging.optJSONObject("cursors");
            if (!cursors.isNull("after"))
            afterString[0] = cursors.optString("after");
            else { noData[0] = true; }
            }
            else { noData[0] = true; }
            }
            else { noData[0] = true; }
            }});
            gr.executeAndWait();


            }while(noData[0] != true);
    }

}
