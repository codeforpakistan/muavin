package com.cfp.muaavin.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 *
 */
public class UrlHelper {



    public static void showUserProfileOnBrowser(String url, Context context)
    {

       /* Intent intent = null;
        try {
            // get the Facebook app if possible
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+url.substring(url.lastIndexOf("/")+1)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Facebook app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        context.startActivity(intent);*/
        try
        {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(facebookIntent);
    }
}
