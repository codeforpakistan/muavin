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
    String[] group = new String[]{"Sexual harassment", "Incitement to violence","Trans rights","All of the above"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3b5998"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        context = this;

    }

    public void blockUser(View view)
    {
        admin.blockUser(context);
    }

    public void deletePost(View view)
    {
        DialogBoxHelper.showDialogBox(admin,  context, group);
    }

    public void viewReportedUser(View view) throws Exception
    {
        admin.viewReportedUser(context);
    }

}

