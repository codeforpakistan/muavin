package com.cfp.muaavin.loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.Comment;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsLoadAsyncTask extends AsyncTask<ArrayList<Post> , Void, ArrayList<Post>> {

    public static FacebookUtil facebookUtil = new FacebookUtil();
    ArrayList<Post> Posts;
    private ProgressDialog dialog;
    AsyncResponsePosts delegate;
    Context context;
    public String userId;
    GraphResponse lastGraphResponse;
    public String user_id;
    public static ArrayList<User> users = new ArrayList<User>();
    public String Post_ID;
    public static boolean PostResponse;
    public  static int count = 0;
    public static GraphRequest nextResultsRequests;
    public ArrayList<Comment> comments = new ArrayList<Comment>();
    public static ArrayList<String>  friendsIds = new ArrayList<String>();
    boolean isClipboardData;
    String Option , Error;


    public PostsLoadAsyncTask(String option, Context context, AsyncResponsePosts delegate , String user_id, boolean isClipboardData, String post_id, ArrayList<Post> Posts, ArrayList<User> users) {

        this.delegate = delegate;
        userId = user_id;
        this.isClipboardData = isClipboardData;
        Post_ID = post_id;
        PostResponse = true;
        this.Posts = Posts;
        this.context = context;
        Option = option;
        Error = "";
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Data, Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        //super.onPreExecute();
    }

    @Override
    protected ArrayList<Post> doInBackground(ArrayList<Post>... params) {

        facebookUtil = new FacebookUtil();
        if(!isClipboardData)
        {
            Posts = getAllPosts(Posts); //  Get All Posts
            while ((FacebookUtil.isUserPresent == false) && (nextResultsRequests != null))
            {
                Posts = getAllPosts(new ArrayList<Post>());
            }
            FacebookUtil.isUserPresent = false; // is Any user found in currently retrievd posts
        }
        else
        {
            Posts = new ArrayList<Post>();
            getPost(Post_ID);
        }
        FacebookUtil.users = users;
        return Posts;
    }

    @Override
    protected void onPostExecute(ArrayList<Post> result)
    {
        if (dialog.isShowing()) { dialog.dismiss(); }
        if(!Error.equals("")) { Toast.makeText(context, Error, Toast.LENGTH_LONG).show();}
        else { delegate.getUserAndPostData(result,Option); }
    }

    public ArrayList<Post> getAllPosts(final ArrayList<Post> posts)
    {
        if(count == 0) {
            Bundle params = new Bundle();
            params.putString("limit","10");
            params.putString("fields", "from{id,name,picture},message,full_picture,story,created_time,picture,comments.summary(true){from{id,name,picture},id,message,comments{from{id,name,picture},id,message}}");//,comments.summary(true)
            new GraphRequest(AccessToken.getCurrentAccessToken(),
            "/"+Post_ID+"/feed",
            params,
            HttpMethod.GET,
            new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
            if(response.getError()==null) {
                lastGraphResponse = response;
                Posts = facebookUtil.getJsonDataPosts(response, posts);
                getCommentsAndReplies();
            } else {Error = "Network Error";}

            }

            }).executeAndWait();


            if(lastGraphResponse!=null)
            nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
            count = count + 1;
        }
        else if(count > 0)
        {
            if (nextResultsRequests != null)
            {
            nextResultsRequests.setCallback(new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
            if(response.getError()==null) {
                lastGraphResponse = response;
                Posts = facebookUtil.getJsonDataPosts(response, posts);
                getCommentsAndReplies();
            } else { Error = "Network Error"; }
            }});
            nextResultsRequests.executeAndWait();
            }
            if(lastGraphResponse!=null)
            nextResultsRequests = lastGraphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        }
        return Posts;
    }

    public void getPost(final String post_id)// get Single post (Clipboard Post)
    {

        Bundle params = new Bundle();
        params.putString("fields", "from{id,name,picture},message,full_picture,story,created_time,picture,comments.summary(true){from{id,name,picture},id,message,comments.limit(900){from{id,name,picture},id,message}}");
        GraphRequest gr = new GraphRequest(
        AccessToken.getCurrentAccessToken(),
        "/"+post_id,params,
        HttpMethod.GET,
        new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                /* handle the result */
                if(response.getError()==null) {
                    JSONObject jObjResponse = response.getJSONObject();
                    Posts.add(facebookUtil.getPost(jObjResponse, 0));
                    getCommentsAndReplies();
                }
                else PostResponse = false; // Post Response - ClipBoard Post

            }
        } );
        gr.executeAndWait();
    }

    public static ArrayList<User> getUsers(){ return users; } // get All Users

    public static boolean getPostResponse() { return PostResponse; } // getPostResponse_ClipBoard

    public void getCommentsAndReplies()
    {
      for(int postID = 0 ;postID < Posts.size() ; postID++)
      {
        final  int post_index = postID;
        if(Posts.get(post_index).comment_count >  0)
        {
            getGraphApiComments(Posts.get(post_index).id, -1, post_index, true);
            comments = Posts.get(post_index).Comments;

            for (int j = 0; j < comments.size(); j++)
            {
                final int comment_index = j;
                final String CommentID = comments.get(j).comment_id;
                if (comments.get(comment_index).reply_count > 0)
                {
                    getGraphApiComments(CommentID, comment_index, post_index, false);
                }
            }
        }

       }
    }

    public  ArrayList<Comment> getJsonComments(ArrayList<Comment> coments,GraphResponse response, String post_id ,String parent_CommentID, int isReply)
    {

        ArrayList<Comment> comments = coments;
        JSONObject content = response.getJSONObject();
        JSONArray data =  content.optJSONArray("data");

        for(int i = 0 ; i < data.length() ; i++)
        {
            JSONObject obj = data.optJSONObject(i);
            JSONObject  from = obj.optJSONObject("from");
            if(from!=null) {
                JSONObject replies = obj.optJSONObject("comments");
                Comment comment = new Comment();
                if (replies != null)
                    if (replies.has("summary")) {
                        comment.reply_count = replies.optJSONObject("summary").optInt("total_count");
                    }

                if (isReply == 1) {
                    comment.setComment(obj.optString("id"), parent_CommentID, from.optString("name"), post_id, from.optString("id"), from.optJSONObject("picture").optJSONObject("data").optString("url"), obj.optString("message"), 0);
                } else {
                    comment.setComment(obj.optString("id"), parent_CommentID, from.optString("name"), post_id, from.optString("id"), from.optJSONObject("picture").optJSONObject("data").optString("url"), obj.optString("message"), comment.reply_count);
                }

                if (!friendsIds.contains(comment.user_id) && (!comment.user_id.equals(userId)) && (!FacebookUtil.BlockedUsersIds.contains(comment.user_id))) {
                    friendsIds.add(comment.user_id);
                    User user = new User();
                    if (from.has("picture")) {
                        user.profile_pic = from.optJSONObject("picture").optJSONObject("data").optString("url");
                    }
                    user.setUserInformation(comment.user_id, comment.name, user.profile_pic, "https://www.facebook.com/" + comment.user_id, "UnBlocked");
                    users.add(user);
                }
                if (isReply == 1) {
                    comment.parent_comment_id = parent_CommentID;
                }
                comments.add(comment);
            }
        }
            return comments;
    }


    public void getGraphApiComments(final String ID , final int comment_index,final int post_index , final boolean isComment)
    {
        final String[] afterString = {""};  // will contain the next page cursor
        final Boolean[] noData = {false};   // stop when there is no after cursor

        do {
                Bundle params = new Bundle();
                params.putString("after", afterString[0]);
                params.putString("fields", "id,comments.summary(true),message,from{id,name,picture},likes");

                GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + ID + "/comments",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                if (response.getError() == null) {
                JSONObject jsonObject = response.getJSONObject();

                if(isComment == true) {
                if (Posts.get(post_index).Comments == null) { Posts.get(post_index).Comments = new ArrayList<Comment>(); }
                 Posts.get(post_index).Comments = getJsonComments(Posts.get(post_index).Comments, response, Posts.get(post_index).id, "", 0);
                }
                else {comments.get(comment_index).replies = getJsonComments(comments.get(comment_index).replies,response, Posts.get(post_index).id, ID, 1);}


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
                }
                });
                gr.executeAndWait();

            }while(noData[0] != true);
    }

    public void getPostDetail(final String post_id)// get Single post (Clipboard Post)
    {

        Bundle params = new Bundle();
        params.putString("fields", "from{id,name,picture},message,full_picture,story,created_time,picture,comments.summary(true){from{id,name,picture},id,message,comments.limit(900){from{id,name,picture},id,message}}");
        GraphRequest gr = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+post_id,params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                /* handle the result */
                        if(response.getError()==null) {
                            JSONObject jObjResponse = response.getJSONObject();
                            Posts.add(facebookUtil.getPost(jObjResponse, 0));
                            getCommentsAndReplies();
                        }
                        else PostResponse = false; // Post Response - ClipBoard Post

                    }
                } );
        gr.executeAndWait();
    }

}
