package com.cfp.muaavin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.muaavin.helper.UrlHelper;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.WebServiceActivity;
import com.cfp.muaavin.web.ImageSelectorAsyncTask;
import com.cfp.muaavin.facebook.User;

import java.util.ArrayList;

/**
 *
 */
public class Higlights_CustomAdapter extends BaseAdapter {

    ArrayList<User> infringing_friends;



    Context context;
    String s;
    private static LayoutInflater inflater=null;



    public Higlights_CustomAdapter(Activity webActivity, ArrayList<User> infringing_friends) {


        context = webActivity;
        this.infringing_friends = infringing_friends;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {


        return infringing_friends.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    public class Holder
    {
        TextView text_view;

        ImageView image;

        Button userProfile;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.highlights_row_layout, null);
        holder.text_view=(TextView) rowView.findViewById(R.id.Textbox1);
        holder.image=(ImageView) rowView.findViewById(R.id.Image_view);
        holder.userProfile = (Button)rowView.findViewById(R.id.UserProfile);

        holder.text_view.setText(" "+ infringing_friends.get(position).name);

        if(infringing_friends.get(position).profile_url.contains("facebook")){
            holder.userProfile.setVisibility(View.VISIBLE);
        }
        else
            holder.userProfile.setVisibility(View.GONE);

        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UrlHelper.showUserProfileOnBrowser(infringing_friends.get(position).profile_url,context);

            }
        });

        new ImageSelectorAsyncTask(holder.image, holder.text_view).execute(infringing_friends.get(position).profile_pic);



        return rowView;
    }

    public  void showUserProfileOnBrowser(String url)
    {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(facebookIntent);

    }


}
