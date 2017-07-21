package com.cfp.muaavin.facebook;

import android.content.Context;
import android.content.SharedPreferences;

import com.cfp.muaavin.helper.SharedPreferenceHelper;
import com.cfp.muaavin.loaders.PostsLoadAsyncTask;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
    public class FacebookUtil  {


    public ArrayList<Post> User_Posts = new ArrayList<Post>();

    public static ArrayList<User> Commented_users;
    public static PostDetail ReportPostDetail = new PostDetail();
    public String user_id= AccessToken.getCurrentAccessToken().getUserId();
    public static ArrayList<String> friendsIds = new ArrayList<String>();
    public static  ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<String> BlockedUsersIds = new ArrayList<String>();
    public static ArrayList<Post> Posts = new ArrayList<Post>();
    public static ArrayList<String> PostIds = new ArrayList<String>();
    public static ArrayList<String> PostIdsSpecific = new ArrayList<String>();
    public static boolean isUserPresent = false; // is Any user found in currently retrievd posts



    public ArrayList<Post> getJsonDataPosts(GraphResponse response, ArrayList<Post> User_Posts) {

        user_id = AccessToken.getCurrentAccessToken().getUserId();
        JSONObject jObjResponse = response.getJSONObject();
        JSONArray data = jObjResponse.optJSONArray("data");

        this.User_Posts = User_Posts;


        for (int i = 0; i < data.length(); i++)
        {
            JSONObject subdata = data.optJSONObject(i);
            int check = 0;
            Post post =  getPost( subdata , check);
            if(!PostIdsSpecific.contains(post.id)) { PostIdsSpecific.add(post.id) ; User_Posts.add(post); }
        }
        return this.User_Posts;
    }


    public String getProfile_pic(GraphResponse response) {

        JSONObject content = response.getJSONObject();
        JSONObject data = content.optJSONObject("data");
        String url = data.optString("url");
        return url;
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
        if(PostDetailObj.IsTwitterPost)  PostDetailObj.PostUrl = "https://twitter.com/"+PostDetailObj.infringing_user_id+"/status/"+PostDetailObj.post_id.split("-")[0];
        else  PostDetailObj.PostUrl = "https://www.facebook.com/"+PostDetailObj.post_id.split("-")[0];

        return PostDetailObj;

    }

    public static ArrayList<User> getFriends() {

        FacebookLoginActivity.friend_list = new ArrayList<User>();
        for(int i = 0 ; i < 13; i++)
        {
           User user = new User();
           user.id = FacebookUtil.users.get(i).id;
           user.name = FacebookUtil.users.get(i).name;
           user.profile_pic = FacebookUtil.users.get(i).profile_pic;
           user.profile_url = "https://www.facebook.com/" + user.id;
           FacebookLoginActivity.friend_list.add(user) ;
        }
        return users;
    }

    public ArrayList<User> getUsers() {

        return users;
    }

    public ArrayList<Comment> getJsonComments(JSONObject json_object, String post_id, String Parent_comment_id, ArrayList<Comment> comments)
    {

        JSONArray data = json_object.optJSONArray("data");

        for (int i = 0; i < data.length(); i++) {

            JSONObject obj = data.optJSONObject(i);
            JSONObject from = obj.optJSONObject("from");
            Comment comment = new Comment();
            comment.name = from.optString("name");
            comment.user_id = from.optString("id");
            comment.comment_id = obj.optString("id");
            comment.parent_comment_id = Parent_comment_id;
            comment.post_id = post_id;
            comment.message = obj.optString("message");

            ////
            JSONObject picture = from.optJSONObject("picture");
            JSONObject picture_data = picture.optJSONObject("data");
            String url = picture_data.optString("url");

            if (obj.has("comments")) {
                JSONObject replies = obj.optJSONObject("comments");
                comments = getJsonComments(replies, post_id, comment.comment_id, comments); // passing the existing comments Arraylist
            }

            if (!friendsIds.contains(comment.user_id)&& (!BlockedUsersIds.contains(comment.user_id)) && (!comment.user_id.equals(user_id))) {
                friendsIds.add(comment.user_id);

                User user = new User();
                user.id = comment.user_id;
                user.name = comment.name;
                user.profile_url = "https://www.facebook.com/" + comment.user_id;
                user.profile_pic = url;


                users.add(user);
            }

            comments.add(comment);

        }

        return comments;

    }


    public Post getPost(JSONObject subdata , int check)
    {
        Post post1 = new Post();
        if (subdata.has("from")) {
            post1.PostOwner.id = subdata.optJSONObject("from").optString("id");
            post1.PostOwner.name = subdata.optJSONObject("from").optString("name");
            post1.PostOwner.profile_pic = subdata.optJSONObject("from").optJSONObject("picture").optJSONObject("data").optString("url");
        }
        if (subdata.has("message")) {
            check = check + 1;
            post1.message += "Message :" + subdata.optString("message") + "\n" + " " + "\n";
        }
        if (subdata.has("id")) {
            post1.id = subdata.optString("id");
            post1.post_url = "https://www.facebook.com/" + post1.id;
            check = check + 1;
        }
        if (subdata.has("story")) {
            check = check + 1;
            post1.message = post1.message + subdata.optString("story") + "\n" + " " + "\n";
        }
        if (subdata.has("created_time")) {
            post1.message = post1.message + "Created Time :" + subdata.optString("created_time") + "\n";
            check = check + 1;
        }
        if (subdata.has("full_picture")) {
            post1.image = subdata.optString("full_picture");
            check = check + 1;
        }
        if (check > 0)// isData Available
        {
            if(subdata.has("comments"))
            {
                post1.comment_count = subdata.optJSONObject("comments").optJSONObject("summary").optInt("total_count");
                if(post1.comment_count > 0) isUserPresent = true;
            }
          // if( subdata.has("comments")){ post1.Comments = getJsonComments(subdata.optJSONObject("comments"), post1.id,"", new ArrayList<Comment>()); }
           if(!PostIds.contains(post1.id)) {  PostIds.add(post1.id); Posts.add(post1); }
        }
        return post1;
    }

    public static  void clearFacebookData()
    {
        FacebookUtil.users = new ArrayList<User>();
        FacebookUtil.Posts = new ArrayList<Post>();
        FacebookUtil.PostIds = new ArrayList<String>();
        FacebookUtil.friendsIds = new ArrayList<String>();
        FacebookUtil.PostIdsSpecific = new ArrayList<String>();

            PostsLoadAsyncTask.nextResultsRequests = null;
            PostsLoadAsyncTask.count = 0;
            PostsLoadAsyncTask.PostResponse = false;
            PostsLoadAsyncTask.friendsIds = new ArrayList<String>();
            PostsLoadAsyncTask.users = new ArrayList<User>();

    }

    public static void disconnectFromFacebook(final Context context) {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.GET, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if(graphResponse.getError()!=null)
                {
                    LoginManager.getInstance().logOut();
                    SharedPreferenceHelper.removeDataFromSharedPreferences(context);
                }
            }
        }).executeAsync();

    }

    public  void removeDataFromSharedPreferences(Context context)
    {
        SharedPreferences mySPrefs = context.getSharedPreferences("Login",0);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("AccessToken");
        editor.remove("ApplicationId");
        editor.remove("UserId");
        editor.remove("Permissions");
        editor.remove("DeclinedPermissions");
        mySPrefs.edit().clear().apply();
        editor.apply();
    }


}
