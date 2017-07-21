package com.cfp.muaavin.BusinessLogic;

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
import android.widget.Toast;

import com.cfp.muaavin.R;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.UrlHelper;
import com.cfp.muaavin.web.ImageAsyncDownload;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.util.ArrayList;

/**
 *
 */
public class UserCustomAdapter extends BaseAdapter {

    ArrayList<User> users;
    Context context;
    private static LayoutInflater inflater=null;
    String option;

    public UserCustomAdapter(Context context , ArrayList<User> users, String option)
    {
        this.context = context;
        this.users = users;
        this.option = option;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class uiWigets
    {
        public TextView userName;

        ImageView image = new ImageView(context);

        Button userProfile = new Button(context);

        Button BlockButton = new Button(context);

        Button UnReport = new Button(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final uiWigets holder = new uiWigets();
        View rowView;
        if(option.equals("Block User"))
        {
            rowView = inflater.inflate(R.layout.user_row_layout, null);
        }
        else
        {
            rowView = inflater.inflate(R.layout.infringinguser_row_layout, null);
            holder.UnReport = (Button) rowView.findViewById(R.id.UnReport);
        }

        holder.BlockButton = (Button) rowView.findViewById(R.id.Block);
        holder.userName =(TextView) rowView.findViewById(R.id.userName);
        holder.image = (ImageView) rowView.findViewById(R.id.Image_view);
        holder.userProfile = (Button) rowView.findViewById(R.id.UserProfile);

        if(users.get(position).state.equals("UnBlocked")) { holder.BlockButton.setText("Block User"); }
        else { holder.BlockButton.setText("UnBlock"); }

        if(users.get(position).isTwitterUser)
        holder.userName.setText(" " + users.get(position).name+" (Twitter)");
        else
        holder.userName.setText(" " + users.get(position).name+" (Facebook)");

        holder.userProfile.setOnClickListener(new View.OnClickListener() { // Show User Post On Browser
            @Override
            public void onClick(View v)
            {
                UrlHelper.showUserProfileOnBrowser(users.get(position).profileUrl, context);
            }
        });

        holder.BlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                if (holder.BlockButton.getText().toString().equals("Block User"))
                {
                    holder.BlockButton.setText("UnBlock"); users.get(position).state = "Blocked";
                    new WebHttpGetReq(context).execute("http://52.176.101.55:8080/Muaavin-Web/rest/Users/BlockUser?user_id=" + AesEncryption.encrypt(users.get(position).id)+"&isTwitterUser="+users.get(position).isTwitterUser);
                } else
                {
                    holder.BlockButton.setText("Block User"); users.get(position).state = "UnBlocked";
                    new WebHttpGetReq(context).execute("http://52.176.101.55:8080/Muaavin-Web/rest/Users/UnBlockUser?user_id=" + AesEncryption.encrypt(users.get(position).id)+"&isTwitterUser="+users.get(position).isTwitterUser);
                }
                } catch (Exception e) { e.printStackTrace(); }

            }
        });

        holder.UnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!users.get(position).state.equals("Blocked")){ // Can Only be UnReported if User is not Blocked
              try
              {
                  users.remove(position);
                  notifyDataSetChanged();
                new WebHttpGetReq(context).execute( "http://52.176.101.55:8080/Muaavin-Web/rest/Users/UnReportUser?user_id=" + AesEncryption.encrypt(users.get(position).id)+"&isTwitterData="+users.get(position).isTwitterUser);
              } catch (Exception e) {  e.printStackTrace(); }
            } else { Toast.makeText(context, "User Already Blocked", Toast.LENGTH_LONG).show(); }

            }
        });

        new ImageAsyncDownload(holder.image).execute(users.get(position).profilePic);

        return rowView;
    }




}

