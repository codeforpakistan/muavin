package com.cfp.muaavin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.ui.Post_ListView;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.Tweet_ListView;
import com.cfp.muaavin.web.DialogBox;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Tooba Saeed on 09/01/2017.
 */

public class Tweets_CustomAdapter extends BaseAdapter {

    ArrayList<Post> TwitterPost;
    Context context;
    private static LayoutInflater inflater=null;
    //public String[] group = {"A","B","C"};
    String[] group = new String[]{"Sexual harassment", "Incitement to violence","Hate speech"};
    public static Tweet_ListView BrowseActivityDelegate;

    public Tweets_CustomAdapter(Context context, ArrayList<Post> tweets, Tweet_ListView delegate) {
        TwitterPost = tweets;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BrowseActivityDelegate = delegate;
    }


    @Override
    public int getCount() {
        return TwitterPost.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  class Holder
    {
        TextView TweetHeading ;
        TextView Tweet;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View rowView = inflater.inflate(R.layout.tweet_row_layout, null);
        Holder holder = new Holder();
        holder.TweetHeading = (TextView) rowView.findViewById(R.id.TweetHeading);
        holder.Tweet = (TextView) rowView.findViewById(R.id.Tweet);
        holder.Tweet.setText(TwitterPost.get(position).message);
        holder.TweetHeading.setText("Tweet : "+TwitterPost.get(position).PostOwner.name);
        holder.Tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterUtil.ReportTwitterDetail.post_id = TwitterPost .get(position).id;
                TwitterUtil.ReportTwitterDetail.post_Detail = removeCharacter(TwitterPost.get(position).message,"'","''");
                TwitterUtil.ReportTwitterDetail.post_image = "ghwjbjbjb";
                TwitterUtil.ReportTwitterDetail.infringing_user_id = TwitterPost.get(position).PostOwner.id;
                TwitterUtil.ReportTwitterDetail.infringing_user_name = TwitterPost.get(position).PostOwner.name;
                TwitterUtil.ReportTwitterDetail.infringing_user_profile_pic = TwitterPost.get(position).PostOwner.profile_pic;
                DialogBox.ShowDialogBOx3(context, "Select Group ", group, 5, "user_signed_inID",null,null, true);
            }
        });



        return rowView;
    }
    public String removeCharacter(String text, String replacedValue, String replaceWith )
    {

        if (text.contains(replacedValue)) {
            text = text.replaceAll(replacedValue, replaceWith);
        }
        return text;
    }
}
