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
import com.cfp.muaavin.helper.DialogBoxHelper;
import com.cfp.muaavin.ui.MenuActivity;
import com.cfp.muaavin.web.ImageAsyncDownload;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 *
 */
public class DialogCustomAdapter extends BaseAdapter {

    public ArrayList<String> Posts;
    Context context;
    private static LayoutInflater inflater=null;
//    public String GroupName;
    Admin admin;
    boolean isFb;
    String option;
    public DialogCustomAdapter(Context context, ArrayList<String> posts, Admin adm)
    {
        this.context = context;
       // GroupName = group_name;
        Posts = posts;
        this.admin = adm;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public DialogCustomAdapter(Context context, ArrayList<String> posts, Admin adm, boolean isFb, String option)
    {
        this.context = context;
        // GroupName = group_name;
        Posts = posts;
        this.admin = adm;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isFb = isFb;
        this.option = option;
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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        View rowView = inflater.inflate(R.layout.dialog_row, null);//
        final Holder holder = getHolder(rowView);

        holder.PostTextview.setText(Posts.get(position));

        holder.CrossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            //    if(Posts.get(position).equals(MenuActivity.group[0]))
                DialogBoxHelper.showDialogBox2(context, MenuActivity.group[position],MenuActivity.groupInfo[position]);
//                else if(Posts.get(position).equals(MenuActivity.group[1]))
//                    DialogBoxHelper.showDialogBox2(context, MenuActivity.group[1],MenuActivity.groupInfo[1]);
//                else if(Posts.get(position).equals(MenuActivity.group[2]))
//                    DialogBoxHelper.showDialogBox2(context, MenuActivity.group[2],MenuActivity.groupInfo[2]);
//                else if(Posts.get(position).equals(MenuActivity.group[3]))
//                    DialogBoxHelper.showDialogBox2(context, MenuActivity.group[3],MenuActivity.groupInfo[3]);
           }
        });

        holder.PostTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String GroupName = DialogBoxHelper.group[position];
                if(option.equals("posts")){
                try {   admin.deletePost(context, GroupName.toString()); }
                catch (Exception e) { e.printStackTrace(); }
                }else if(option.equals("highlightedPosts")){
                try {   admin.browsePosts(context, GroupName.toString()); }
                catch (Exception e) { e.printStackTrace(); }
                }else if(option.equals("highlightedTweets")){
                try {   admin.browseTweets(context, GroupName.toString()); }
                catch (Exception e) { e.printStackTrace(); }
            }
                DialogBoxHelper.alertDialog.dismiss();
            }
        });

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
        holder.PostTextview = (TextView)rowView.findViewById(R.id.textView3);
        holder.CrossButton = (Button)rowView.findViewById(R.id.button);
        return holder;
    }


}
