package com.cfp.muaavin.BusinessLogic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 */
public class UrlHelper {

    public static String getDecodedUrl(String imageUrl)
    {
        try {
            imageUrl = URLDecoder.decode(imageUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return imageUrl;
    }

    public static String getEncodedUrl(String imageUrl)
    {
        try {
            imageUrl = URLEncoder.encode(imageUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return imageUrl;
    }

    /*public static void showDataOnBrowser(Context context ,String url)
    {
        try{
          context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(facebookIntent);
    }*/


    public static void showDataOnBrowser(Context context ,String url)
    {

        String id = url.substring(url.lastIndexOf("/")+1);

        Intent intent = null;

        String ids[] = id.split("_");
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/"+ids[0]+"/posts/"+ids[1]));
        context.startActivity(intent);

    }
    public static String getQueryFieldsFromURL(String url) // Will Return Post ID from URL
    {
        String post_id = "";
        Uri uri = Uri.parse(url);
        String story_fbid = uri.getQueryParameter("story_fbid");
        String id = uri.getQueryParameter("id");
        post_id = id+"_"+story_fbid;
        return post_id;
    }
}

