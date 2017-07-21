package com.cfp.muaavin.twitter;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tooba Saeed on 07/01/2017.
 */

public interface CustomService {

    @GET("/1.1/followers/list.json?include=metadata")
    Call<ResponseBody> show(@Query("user_id") long id, @Query("cursor") long cursor /*, Callback<Response> cb*/ );

    @GET("/1.1/statuses/home_timeline.json")
    Call<ResponseBody> showHomeTimelineTweets(@Query("user_id") long id , @Query("cursor") String cursor  );

    @GET("/1.1/users/show.json")
    Call<User> getLoggedInUser(@Query("user_id") long id );

    @GET("/1.1/statuses/show.json")
    Call<Tweet> getTweetUsingId(@Query("id") String id );


}
