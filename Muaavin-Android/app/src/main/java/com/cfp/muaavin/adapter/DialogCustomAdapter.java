package com.cfp.muaavin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cfp.muaavin.helper.DataLoaderHelper;
import com.cfp.muaavin.ui.MenuActivity;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.UiUpdate;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.FriendManagement;

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
    Activity activity;
    UiUpdate uiUpdate;
    boolean isTwitterData;
    int option;
    public DialogCustomAdapter(Context context, ArrayList<String> posts, final Activity activity, final UiUpdate uiUpdate, final boolean isTwitterData, int option)
    {
        this.context = context;
       // GroupName = group_name;
        Posts = posts;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isTwitterData = isTwitterData;
        this.activity = activity;
        this.uiUpdate = uiUpdate;
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

        holder.PostTextview.setText(Html.fromHtml(Posts.get(position)));
        if(Posts.get(position).toString().contains("All of the above"))
        {
            holder.CrossButton.setVisibility(View.GONE);
        }

        holder.CrossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                if(Posts.get(position).equals(MenuActivity.group[0]))
                DialogBox.showDialogBox2(context, FriendManagement.group1[position], FriendManagement.groupInfo[position]);
  /*              else if(Posts.get(position).equals(MenuActivity.group[1]))
                    DialogBox.showDialogBox2(context, MenuActivity.group[1], MenuActivity.groupInfo[1]);
                else if(Posts.get(position).equals(MenuActivity.group[2]))
                    DialogBox.showDialogBox2(context, MenuActivity.group[2], MenuActivity.groupInfo[2]);
                else if(Posts.get(position).equals(MenuActivity.group[3]))
                    DialogBox.showDialogBox2(context, MenuActivity.group[3], MenuActivity.groupInfo[3]);
  */          }
        });

        holder.PostTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String dataType = "Facebook";
                String group_name = DialogBox.group[position];
                DialogBox.CategoryName = Posts.get(position);
                DataLoaderHelper dataLoader = new DataLoaderHelper(context,uiUpdate,activity);
                if(isTwitterData) dataType = "Twitter";
                try {
                    dataLoader.loadReportedUsersFromDB(dataType,group_name,position,option,activity, uiUpdate);
                } catch (Exception e) { e.printStackTrace(); }
                DialogBox.alertDialog.dismiss();
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
