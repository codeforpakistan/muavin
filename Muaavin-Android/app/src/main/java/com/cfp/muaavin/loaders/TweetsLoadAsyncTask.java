package com.cfp.muaavin.loaders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.twitter.MyTwitterApiClient;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Tooba Saeed on 04/01/2017.
 */

public class TweetsLoadAsyncTask extends AsyncTask<ArrayList<Post>, Void, ArrayList<Post>> {

    public static ArrayList<User> users;
    public static long FollowersCursor = -1l;
    public static long maxId = 0l;
    public Context context;
    public TwitterUtil twitterUtil = new TwitterUtil();
    public String option;
    ArrayList<Post> Tweets = new ArrayList<Post>();
    List<Tweet> TweetsResponse;
    Call<List<Tweet>> TweetList;
    ResponseBody responseBody;
    com.twitter.sdk.android.core.models.User user;
    TweetsAsynchronousResponse TweetsDelegate;
    private ProgressDialog dialog;


    public TweetsLoadAsyncTask(Context contex, TweetsAsynchronousResponse tweetsDelegate, String option) {
        context = contex;
        TweetsDelegate = tweetsDelegate;
        this.option = option;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Data, Please Wait..");
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Post> doInBackground(ArrayList<Post>... params) {


        final StatusesService service = Twitter.getInstance().getApiClient().getStatusesService();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        Call<ResponseBody> CallResponseBody = null;
        Call<com.twitter.sdk.android.core.models.User> CallUser = null;
        Call<Tweet> CallTweet = null;
        if (option.equals("LoadTweets")) {
            if (maxId == 0l) {
                TweetList = service.homeTimeline(null, null, null, null, null, null, null);
            } else {
                TweetList = service.homeTimeline(null, null, maxId, null, null, null, null);
            }

            try {
                TweetsResponse = TweetList.execute().body();
                if (TweetsResponse != null)
                    maxId = TweetsResponse.get(TweetsResponse.size() - 1).id;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (TweetsResponse != null) {
                Tweets = twitterUtil.getTweets(TweetsResponse);
            }

            return Tweets;
        } else if (option.equals("Load Specific Tweet")) {
            CallTweet = new MyTwitterApiClient(session).getCustomService().getTweetUsingId("828139684148682752");
            try {
                TwitterUtil.Tweet = CallTweet.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Tweets;
        } else if (option.equals("LoadUser")) {
            CallUser = new MyTwitterApiClient(session).getCustomService().getLoggedInUser(session.getUserId());
            try {
                user = CallUser.execute().body();
                TwitterUtil.user.setUserInformation(user.idStr, user.name, user.profileImageUrl, user.profileBannerUrl, "UnBlocked");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Tweets;
        } else if (option.equals("LoadFollowers")) {
            CallResponseBody = new MyTwitterApiClient(session).getCustomService().show(session.getUserId(), FollowersCursor);
        }

        try {
            responseBody = CallResponseBody.execute().body();
            getDataFromResponseBody(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Tweets;
    }

    @Override
    protected void onPostExecute(ArrayList<Post> result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        TweetsDelegate.tweetsAsynchronousResponse(Tweets, option);

    }

    public void getDataFromResponseBody(ResponseBody responseBody) {
        String response = "";
        try {
            response = IOUtils.toString(responseBody.source().inputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonData = response;
        JSONObject Jobject = null;
        JSONArray JArray = null;
        if (option.equals("LoadFollowers")) {
            try {
                Jobject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray Jarray = Jobject.optJSONArray("users");
            ArrayList<User> users = new ArrayList<User>();
            for (int i = 0; i < Jarray.length(); i++) {
                User user = new User();
                JSONObject object = Jarray.optJSONObject(i);
                user.name = object.optString("name");
                user.id = object.optString("id_str");
                user.profile_pic = object.optString("profile_image_url");
                user.profile_url = "https://twitter.com/" + object.optString("screen_name");
                user.state = "UnBlocked";
                users.add(user);
                TwitterUtil.Followers.add(user);
            }
            FollowersCursor = Jobject.optLong("next_cursor_str");
        }
    }

}
