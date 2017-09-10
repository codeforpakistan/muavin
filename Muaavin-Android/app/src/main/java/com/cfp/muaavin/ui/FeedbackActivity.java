package com.cfp.muaavin.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.web.WebHttpGetReq;
import com.facebook.Profile;

public class FeedbackActivity extends ActionBarActivity implements FeedbackInterface {

    EditText etcomments = null;
    Button send;
    RatingBar rating;
    String serverURL="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        etcomments = (EditText)findViewById(R.id.comments);
        send = (Button)findViewById(R.id.send);
        rating = (RatingBar)findViewById(R.id.ratingbar);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stars = (int) rating.getRating();
                String comments = etcomments.getText().toString();

                if(stars!=0 && comments !=null && (!comments.equals(""))){
                    try {

                        //      serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/FeedBack/Add_FeedBack?user_id=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) +"&InfringingUserId="+AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+ "&post_id=" + AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id )+ "&comment=" +AesEncryption.encrypt( comment)+"&IsTwitterFeedBack="+TwitterFeedBack+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                        serverURL = MenuActivity.baseURL+"Muaavin-Web/rest/muaavinFeedBack/Add_MuaavinFeedBack?user_id=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) +"&rating="+AesEncryption.encrypt(String.valueOf(stars))+"&comments="+AesEncryption.encrypt(comments);
                    } catch (Exception e) { e.printStackTrace(); }
                    new WebHttpGetReq(FeedbackActivity.this,FeedbackActivity.this, 1010, FeedbackActivity.this).execute(serverURL);


                }

            }
        });

    }

    @Override
    public void getResult(String result) {

        if(result.equals(etcomments.getText().toString()))
        {
            Toast.makeText(FeedbackActivity.this,"Successful",Toast.LENGTH_SHORT).show();
            FeedbackActivity.this.finish();
        }
        else
            Toast.makeText(FeedbackActivity.this,"Failure",Toast.LENGTH_SHORT).show();

    }


}
