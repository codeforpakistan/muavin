package com.cfp.muaavin.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.muaavin.facebook.Group;
import com.cfp.muaavin.helper.DataLoaderHelper;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.web.GroupImageAsyncTask;
import com.cfp.muaavin.web.ImageSelectorAsyncTask;

import java.util.ArrayList;

import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;

/**
 * Created by Tooba Saeed on 01/03/2017.
 */

public class GroupsAdapter extends BaseAdapter {

    ArrayList<Group> Groups;
    Context context;
    private static LayoutInflater inflater=null;
    Activity activity;

    public GroupsAdapter(ArrayList<Group> groups, Context contex, Activity activity)
    {
        context = contex;
        Groups  = groups;
        this.activity = activity;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return Groups.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    class Holder
    {
        TextView GroupText;
        ImageView groupIcon;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View rowView = inflater.inflate(R.layout.group_row_layout, null);
        Holder holder = getHolder(rowView );
        holder.GroupText.setText(Groups.get(position).GroupName);

//        new GroupImageAsyncTask(holder.groupIcon).execute(Groups.get(position).GroupId);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataLoaderHelper controller = new DataLoaderHelper(context,null,activity);
                clearFacebookData();
                controller.loadFacebookPosts("GroupPosts",Groups.get(position).GroupId,false);
            }
        });

        return rowView;
    }

    public Holder getHolder(View rowView)
    {
        Holder holder = new Holder();
        holder.GroupText = (TextView)rowView.findViewById(R.id.GroupName);
        holder.groupIcon = (ImageView)rowView.findViewById(R.id.Image);
        return holder;
    }
}
