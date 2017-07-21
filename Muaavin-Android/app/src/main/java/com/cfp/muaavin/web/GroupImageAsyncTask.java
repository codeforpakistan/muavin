package com.cfp.muaavin.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.io.InputStream;


public class GroupImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;


    public GroupImageAsyncTask(ImageView bmImage) {
        this.bmImage = bmImage;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }
    @Override
    protected Bitmap doInBackground(String... params) {
      /*  String url = params[0];

        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }*/

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{"+params[0]+"}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.i("response",String.valueOf(response));
                    }
                }
        ).executeAsync();


        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        bmImage.setImageBitmap(result);


    }


}