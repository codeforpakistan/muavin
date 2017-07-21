package com.cfp.muaavin.BusinessLogic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cfp.muaavin.R;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.web.ImageAsyncDownload;
import com.cfp.muaavin.web.WebHttpGetReq;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 *
 */
public class PostCustomAdapter extends BaseAdapter {

    public ArrayList<Post> Posts;
    Context context;
    private static LayoutInflater inflater=null;
    public String GroupName;

    public PostCustomAdapter(Context context, ArrayList<Post> posts, String group_name)
    {
        this.context = context;
        GroupName = group_name;
        Posts = posts;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return Posts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {

        TextView PostTextview;
        Button CrossButton;
        ImageView post_image;
        TextView PostHeading;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        View rowView = inflater.inflate(R.layout.post_row_layout, null);//
        final Holder holder = getHolder(rowView);

        holder.PostTextview.setText(Posts.get(position).message);
        if(Posts.get(position).IsTwitterPost)
        {
            holder.PostHeading.setBackgroundColor(Color.parseColor("#00BFFF"));
            holder.PostHeading.setText("Tweet : "+Posts.get(position).PostOwner.name);
        }
        else if(Posts.get(position).IsComment)
        {
            holder.PostHeading.setText("Comment : "+Posts.get(position).PostOwner.name);
        }
        else
        {
            holder.PostHeading.setText("Post : "+Posts.get(position).PostOwner.name);
        }

        holder.CrossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String serverURL = null;
                try
                {
                    serverURL = "http://52.176.101.55:8080/Muaavin-Web/rest/Posts_Query/DeletePosts?Post_id=" + AesEncryption.encrypt(Posts.get(position).id) + "&isPostOfSpecificUser="+false+"&InfringingUserID="+AesEncryption.encrypt(Posts.get(position).PostOwner.id)+"&IsTwitterPost="+Posts.get(position).IsTwitterPost +"&IsComment="+Posts.get(position).IsComment;
                }   catch (Exception e) { e.printStackTrace(); }
                Posts.remove(position);
                notifyDataSetChanged();
                new WebHttpGetReq(context).execute(serverURL);
            }
        });

        holder.PostHeading.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPostOnBrowser(Posts.get(position).postUrl);
            }
        });

        Posts.get(position).image = getDecodedImage((Posts.get(position).image));
            new ImageAsyncDownload(holder.post_image).execute(Posts.get(position).image);

        return rowView;
    }

    public String getDecodedImage(String image)
    {
        try {
            image = URLDecoder.decode(image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return image;
    }

    public void showPostOnBrowser(String url)
    {
        try
        {
          context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(facebookIntent);
    }

    public Holder getHolder(View rowView)
    {
        Holder holder = new Holder();
        holder.PostTextview = (TextView)rowView.findViewById(R.id.Textbox1);
        holder.PostHeading = (TextView)rowView.findViewById(R.id.Textbox2);
        holder.CrossButton = (Button)rowView.findViewById(R.id.CrossButton);
        holder.post_image =  (ImageView)rowView.findViewById(R.id.Image_view);
        return holder;
    }


}
