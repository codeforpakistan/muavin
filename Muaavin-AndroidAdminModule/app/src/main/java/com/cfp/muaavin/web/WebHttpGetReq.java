package com.cfp.muaavin.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.cfp.muaavin.BusinessLogic.Post;
import com.cfp.muaavin.BusinessLogic.User;
import com.cfp.muaavin.BusinessLogic.UserInterface;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.ImageHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 *
 */
public class WebHttpGetReq extends AsyncTask<String, Void, Void> {

    private String Content;


    public static Context context ;
    private String Error = null;
    private ProgressDialog Dialog ;
    String data ="";
    UserInterface UsersDelagate;
    String option;

    public   WebHttpGetReq(String Option, Context contex,  UserInterface users_delegate)
    {
        context  = contex;
        option = Option;
        Dialog = new ProgressDialog(context);
        UsersDelagate = users_delegate;
    }

    public   WebHttpGetReq(Context contex)
    {
        context  = contex;
        Dialog = new ProgressDialog(context);
    }
    @Override
    protected void onPreExecute()
    {
        Dialog.setMessage("Please wait..");
        Dialog.show();
        try { data +="&" + URLEncoder.encode("data", "UTF-8") + "=";}

        catch (UnsupportedEncodingException e){  e.printStackTrace();}
    }

    @Override
    protected Void doInBackground(String... params)
    {
        BufferedReader reader=null;

        try {
            URL url = new URL(params[0]);
            //////////
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet= new HttpGet(params[0]);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            //////////
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(/*conn.getInputStream()*/is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) { sb.append(line + ""); }  // Append server response in string

            Content = sb.toString();


            } catch(Exception ex){ Error = ex.getMessage(); ex.printStackTrace();}

        finally{ try{ reader.close(); } catch(Exception ex) {} }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused)
    {
        Dialog.dismiss();

        if (Error != null){ Content = "Output : "+Error;}
        else
        {
        try
        {
            Content = AesEncryption.decrypt(Content);

            if((option.equals("Block User"))||(option.equals("View Reported Users")))
            {
                JSONArray jsonArray = new JSONArray(Content);
                ArrayList<User> Users = new ArrayList<User>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    User user = getUserDataFromWeb(jsonArray.optJSONObject(i));
                    Users.add(user);
                }

                UsersDelagate.getAsyncResponseUsers(Users);
            }

            if(option.equals("Delete Post"))
            {
                JSONArray jsonArray = new JSONArray(Content);
                ArrayList<Post> Posts = new ArrayList<Post>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Post post = getPostDataFromWeb(jsonArray.optJSONObject(i));
                    Posts.add(post);
                }
                UsersDelagate.getAsyncResponsePosts(Posts);
            }

         }
         catch (JSONException e){ e.printStackTrace();}
         catch (Exception e) {
             e.printStackTrace();}
        }
    }

    public User getUserDataFromWeb(JSONObject jsonResponse)
    {
        User user = new User();
        user.name = jsonResponse.optString("User_Name");
        user.id = jsonResponse.optString("User_ID");
        user.state = jsonResponse.optString("State");
        user.profilePic = ImageHelper.getDecodedImage(jsonResponse.optString("Profile_Pic"));
        user.profileUrl = "https://www.facebook.com/"+user.id;
        user.isTwitterUser = jsonResponse.optBoolean("IsTwitterUser");
        return user;
    }

    public Post getPostDataFromWeb(JSONObject jsonResponse)
    {
        Post post = new Post();
        post.PostOwner.id = jsonResponse.optString("infringingUserId");
        post.PostOwner.name = jsonResponse.optString("infringingUser_name");
        post.message = jsonResponse.optString("Post_Detail");
        post.id = jsonResponse.optString("Post_ID");
        post.image = jsonResponse.optString("Post_Image");
        post.IsTwitterPost = jsonResponse.optBoolean("IsTwitterPost");
        post.IsComment = jsonResponse.optBoolean("IsComment");
        post.postUrl = "https://www.facebook.com/"+post.id;


        return post;
    }


}
