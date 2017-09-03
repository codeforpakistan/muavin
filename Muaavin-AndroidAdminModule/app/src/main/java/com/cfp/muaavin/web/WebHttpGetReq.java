package com.cfp.muaavin.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import com.cfp.muaavin.BusinessLogic.Highlighted_CustomAdapter;
import com.cfp.muaavin.BusinessLogic.Post;
import com.cfp.muaavin.BusinessLogic.User;
import com.cfp.muaavin.BusinessLogic.UserInterface;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.ImageHelper;
import com.cfp.muaavin.helper.PostDetail;
import com.cfp.muaavin.ui.HighlightedPostsActivity;
import com.cfp.muaavin.ui.PostListView;

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
import java.util.HashMap;

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
    int check=0;
    int ItemPosition;
    Highlighted_CustomAdapter.UiUpdate BrowseLayoutDelegate;
    static HashMap<String, ArrayList<PostDetail>> dictionary;
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
    public   WebHttpGetReq(Context c, int check, int value, Highlighted_CustomAdapter.UiUpdate BrowseLayout)
    {
        context  = c;
        this.check = check;
        Dialog = new ProgressDialog(context);
        ItemPosition = value;
        option = "";
        BrowseLayoutDelegate = BrowseLayout;
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

            if(option.equals("highlightedPosts"))
            {
                getReportedPostDetailFromDB( Content, true);
            }

            if(option.equals("highlightedTweets"))
            {
                getReportedPostDetailFromDB( Content, false);            }
            if(check==10)
                BrowseLayoutDelegate.updateFeedBack(ItemPosition,Content);


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

    // Get Reports From WebService
    public void getReportedPostDetailFromDB(String Content, boolean isFb) throws JSONException {

        JSONArray jsonArray = new JSONArray(Content);
        ArrayList<PostDetail> JsonPostDetails = new ArrayList<PostDetail>();
        HashMap<String, ArrayList<PostDetail>> dictionary = new HashMap<String, ArrayList<PostDetail>>();

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonChildNode = jsonArray.optJSONObject(i);
            PostDetail PostDetailObj = getReportedPostDetail(i,jsonChildNode);
            if(isFb){
                //if(PostDetailObj.User_ID.equals(User.getLoggedInUserInformation().id)) {
                JsonPostDetails.add(PostDetailObj);

                if (dictionary.containsKey(PostDetailObj.post_id)) {
                    ArrayList<PostDetail> reportedPostDetail = (ArrayList<PostDetail>) dictionary.get(PostDetailObj.post_id);
                    if (!PostDetailObj.FeedBackMessage.equals("")) {
                        reportedPostDetail.get(0).FeedBacks.add(PostDetailObj.FeedBackMessage);
                    }
                    dictionary.put(PostDetailObj.post_id, reportedPostDetail);
                } else {
                    ArrayList<PostDetail> PostDetailslist = new ArrayList<PostDetail>();
                    if (!PostDetailObj.FeedBackMessage.equals("")) {
                        PostDetailObj.FeedBacks.add(PostDetailObj.FeedBackMessage);
                    }
                    PostDetailslist.add(PostDetailObj);
                    if (isFb) {
                        if (PostDetailObj.IsTwitterPost) {
//                   if(!TwitterUtil.BlockedUserIds.contains(PostDetailObj.infringing_user_id)) dictionary.put(PostDetailObj.post_id,PostDetailslist);
                        }
                        if (PostDetailObj.IsComment) {
                           // if (!FacebookUtil.BlockedUsersIds.contains(PostDetailObj.infringing_user_id))
                                dictionary.put(PostDetailObj.post_id, PostDetailslist);
                        }
                    } else {
                        if (PostDetailObj.IsTwitterPost) {
                            //if(!TwitterUtil.BlockedUserIds.contains(PostDetailObj.infringing_user_id))
                                dictionary.put(PostDetailObj.post_id, PostDetailslist);
                        }
                        if (PostDetailObj.IsComment) {
                            //           if(!FacebookUtil.BlockedUsersIds.contains(PostDetailObj.infringing_user_id)) dictionary.put(PostDetailObj.post_id,PostDetailslist);
                        }
                    }
                }
        /*}else
            continue;*/
            }
            else
            {
                String TwitterUserId="";
                //if(session==null) { TwitterUserId = "";  } else { TwitterUserId = String.valueOf(session.getUserId()); }
                //if(PostDetailObj.User_ID.equals(TwitterUserId)) {
                JsonPostDetails.add(PostDetailObj);

                if (dictionary.containsKey(PostDetailObj.post_id)) {
                    ArrayList<PostDetail> reportedPostDetail = (ArrayList<PostDetail>) dictionary.get(PostDetailObj.post_id);
                    if (!PostDetailObj.FeedBackMessage.equals("")) {
                        reportedPostDetail.get(0).FeedBacks.add(PostDetailObj.FeedBackMessage);
                    }
                    dictionary.put(PostDetailObj.post_id, reportedPostDetail);
                } else {
                    ArrayList<PostDetail> PostDetailslist = new ArrayList<PostDetail>();
                    if (!PostDetailObj.FeedBackMessage.equals("")) {
                        PostDetailObj.FeedBacks.add(PostDetailObj.FeedBackMessage);
                    }
                    PostDetailslist.add(PostDetailObj);
                    if (isFb) {
                        if (PostDetailObj.IsTwitterPost) {
//                   if(!TwitterUtil.BlockedUserIds.contains(PostDetailObj.infringing_user_id)) dictionary.put(PostDetailObj.post_id,PostDetailslist);
                        }
                        if (PostDetailObj.IsComment) {
                    //        if (!FacebookUtil.BlockedUsersIds.contains(PostDetailObj.infringing_user_id))
                                dictionary.put(PostDetailObj.post_id, PostDetailslist);
                        }
                    } else {
                        if (PostDetailObj.IsTwitterPost) {
                      //      if (!TwitterUtil.BlockedUserIds.contains(PostDetailObj.infringing_user_id))
                                dictionary.put(PostDetailObj.post_id, PostDetailslist);
                        }
                        if (PostDetailObj.IsComment) {
                            //           if(!FacebookUtil.BlockedUsersIds.contains(PostDetailObj.infringing_user_id)) dictionary.put(PostDetailObj.post_id,PostDetailslist);
                        }
                    }
                }
          /*  }else
                continue;*/
            }
        }

        Intent intent  = new Intent(context , HighlightedPostsActivity.class);
        intent.putExtra("posts",dictionary);
        intent.putExtra("option",isFb);
        context.startActivity(intent);
  //      return posts;
//        PostDetailDelegate.getPostsDetails(dictionary);
    }


    public PostDetail getReportedPostDetail(int index, JSONObject jsonChildNode) {

        PostDetail PostDetailObj = new PostDetail();

        if(jsonChildNode.has("User_ID")) { PostDetailObj.User_ID = jsonChildNode.optString("User_ID"); }
        if(jsonChildNode.has("Parent_CommentID")) { PostDetailObj.ParentComment_ID = jsonChildNode.optString("Parent_CommentID"); }
        if(jsonChildNode.has("infringingUser_name")) { PostDetailObj.infringing_user_name = jsonChildNode.optString("infringingUser_name"); }
        if(jsonChildNode.has("infringingUserId")) { PostDetailObj.infringing_user_id = jsonChildNode.optString("infringingUserId"); }
        if(jsonChildNode.has("infringingUser_ProfilePic")) { PostDetailObj.infringing_user_profile_pic = jsonChildNode.optString("infringingUser_ProfilePic"); }
        if(jsonChildNode.has("CommentID")) { PostDetailObj.coment_id = jsonChildNode.optString("CommentID"); }
        if(jsonChildNode.has("Post_ID")) { PostDetailObj.post_id = jsonChildNode.optString("Post_ID"); }
        if(jsonChildNode.has("Post_Detail")) { PostDetailObj.post_Detail = jsonChildNode.optString("Post_Detail"); }
        if(jsonChildNode.has("Post_Image")) { PostDetailObj.post_image = jsonChildNode.optString("Post_Image"); }
        if(jsonChildNode.has("unlike_value")) { PostDetailObj.unlike_value = jsonChildNode.optInt("unlike_value"); }
        if(jsonChildNode.has("IsTwitterPost")) { PostDetailObj.IsTwitterPost = jsonChildNode.optBoolean("IsTwitterPost"); }
        if(jsonChildNode.has("FeedBackMessage")) { PostDetailObj.FeedBackMessage = jsonChildNode.optString("FeedBackMessage"); }
        if(jsonChildNode.has("IsComment")) { PostDetailObj.IsComment = jsonChildNode.optBoolean("IsComment"); }
        if(jsonChildNode.has("count")) { PostDetailObj.count = jsonChildNode.optInt("count"); }
        if(PostDetailObj.IsTwitterPost)  PostDetailObj.PostUrl = "https://twitter.com/"+PostDetailObj.infringing_user_id+"/status/"+PostDetailObj.post_id.split("-")[0];
        else  PostDetailObj.PostUrl = "https://www.facebook.com/"+PostDetailObj.post_id.split("-")[0];

        return PostDetailObj;

    }

    public void getResults(HighlightedPostsActivity delegate){
        delegate.getPostsDetails(dictionary);
    }
}
