package com.cfp.muaavin.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.cfp.muaavin.R;
import com.cfp.muaavin.BusinessLogic.Admin;
import com.cfp.muaavin.helper.DialogBoxHelper;


public class MenuActivity extends ActionBarActivity {
    //169.254.68.212
    //13.76.175.64
    Admin admin = new Admin();
    Context context;
    //String[] group = {"A", "B", "C","All"};
    public static String[] group = new String[]{"Sexual harassment", "Incitement to violence","Hate Speech","All of the above"};
    public static String[] groupInfo = new String[]{"*Sexual Harassment* includes all kinds of unwanted sexual advances, comments and associations.", "*Incitement to Violence* is speech that directly calls for violent actions against an individual or group.","*Hate speech* is speech that attacks people on basis of personal attributes like community and gender etc.","All of the above Info"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        context = this;

    }

    public void blockUser(View view)
    {
        admin.blockUser(context);
    }

    public void deletePost(View view)
    {
        DialogBoxHelper.showDialogBox(admin,  context, group,false,"posts");
    }

    public void highlightedPosts(View view) throws Exception
    {
        DialogBoxHelper.showDialogBox(admin,  context, group, true,"highlightedPosts");
    }
    public void highlightedTweets(View view) throws Exception
    {
        DialogBoxHelper.showDialogBox(admin,  context, group,false,"highlightedTweets");
    }
    public void viewReportedUser(View view) throws Exception
    {
        admin.viewReportedUser(context);
    }

}

