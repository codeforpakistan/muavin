package com.cfp.muaavin.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cfp.muaavin.BusinessLogic.Admin;
import com.cfp.muaavin.BusinessLogic.Highlighted_CustomAdapter;
import com.cfp.muaavin.BusinessLogic.User;
import com.cfp.muaavin.R;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.PostDetail;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.support.v7.appcompat.R.styleable.Toolbar;

public class HighlightedPostsActivity extends ActionBarActivity implements Highlighted_CustomAdapter.UiUpdate {

    public Context context;
    ListView list_view;
    String Group_name;
    HashMap<String, ArrayList<PostDetail>> PostDetails;

    ProgressDialog progressDialog = null;
    boolean isFb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlighted_posts);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        list_view = (ListView)findViewById(R.id.listView1);

        isFb = getIntent().getBooleanExtra("option",false);
        PostDetails = (HashMap<String, ArrayList<PostDetail>>) getIntent().getSerializableExtra("posts");

        if(isFb)
            getSupportActionBar().setTitle("Notified Posts");
        else
            getSupportActionBar().setTitle("Notified Tweets");

        Highlighted_CustomAdapter c = new Highlighted_CustomAdapter(HighlightedPostsActivity.this
                ,this, PostDetails);
        list_view.setAdapter(c);
      /*  WebHttpGetReq request = new WebHttpGetReq(HighlightedPostsActivity.this);
        request.getResults(this);
*/
    }

    @Override
    public void getPostsDetails(HashMap<String, ArrayList<PostDetail>> result) {

        PostDetails = result;

    }

    @Override
    public void updateDislikeButton(int position, String response)
    {
        if(response.equals("Record Deleted"))
        {
            final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
            PostDetails.get(keys.get(position)).get(0).unlike_value--;
        }
        else
        {
            final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
            PostDetails.get(keys.get(position)).get(0).unlike_value++;
        }
        ((BaseAdapter) list_view.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void updateFeedBack(int position,String comment) {
        final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
        PostDetails.get(keys.get(position)).get(0).FeedBacks.remove(comment);//add(comment);
        ((BaseAdapter) list_view.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void postLink(String type, String postLink, String userProfile, String message, String userName) {


    }



    public void showLoading(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable
                (new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(new ProgressBar(context));
    }

    public void hideLoading() {
        progressDialog.setCancelable(true);
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
