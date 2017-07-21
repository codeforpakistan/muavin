package com.cfp.muaavin.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


public class ImageSelectorAsyncTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;


    public ImageSelectorAsyncTask(ImageView bmImage, TextView tv1) {
        this.bmImage = bmImage;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];

        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        bmImage.setImageBitmap(result);


    }


}