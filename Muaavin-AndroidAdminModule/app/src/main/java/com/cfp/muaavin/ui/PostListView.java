package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.cfp.muaavin.R;
import com.cfp.muaavin.BusinessLogic.Post;
import com.cfp.muaavin.BusinessLogic.PostCustomAdapter;

import java.util.ArrayList;

public class PostListView extends ActionBarActivity {

    Context context;
    ListView postListView;
    String Group_name;
    ArrayList<Post> Posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list_view);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        context = this;
        postListView = (ListView)findViewById(R.id.PostListview);

        Intent intent =  getIntent();
        Group_name = intent.getStringExtra("Group_name");
        Posts = (ArrayList<Post>) getIntent().getSerializableExtra("posts");

        PostCustomAdapter c = new PostCustomAdapter(context, Posts, Group_name);
        postListView.setAdapter(c);

    }




}
