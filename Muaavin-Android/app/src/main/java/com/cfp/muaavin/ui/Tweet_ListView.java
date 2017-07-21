package com.cfp.muaavin.ui;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.cfp.muaavin.adapter.Tweets_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.loaders.TweetsLoadAsyncTask;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;
import java.util.ArrayList;

public class Tweet_ListView extends Fragment implements TweetsAsynchronousResponse {

    ListView TweetListView;
    Context context;
    String option;
    Button LoadTweets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view  = inflater.inflate(R.layout.tweets_layout, container, false);
        context = getActivity();
        TweetListView = (ListView)view.findViewById(R.id.TweetList);//LoadTweets
        LoadTweets = (Button)view.findViewById(R.id.LoadTweets);
        option = "LoadTweets";

        Tweets_CustomAdapter customAdapter = new Tweets_CustomAdapter(context,TwitterUtil.Tweets);
        TweetListView.setAdapter(customAdapter);

        LoadTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TweetsLoadAsyncTask(context, Tweet_ListView.this,option).execute();
            }
        });
        return view;

    }

    public void loadTweets(View view)
    {
        new TweetsLoadAsyncTask(context, Tweet_ListView.this,option).execute();
    }


    @Override
    public void tweetsAsynchronousResponse(ArrayList<Post> tweet, String option)
    {
        ((BaseAdapter) TweetListView.getAdapter()).notifyDataSetChanged();
    }

}
