package com.cfp.muaavin.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 *
 */
public class UrlHelper {




/*
    public static void showUserProfileOnBrowser(String url, Context context)
    {
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
*/


    public static void showUserProfileOnBrowser(String url, Context context)
    {
        String id = url.substring(url.lastIndexOf("/")+1);

       /* Intent intent;
        *//*try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/"+id)); //Trys to make intent with FB's URI
        } catch (Exception e) {*//*
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/app_scoped_user_id/"+id)); //catches and opens a url to the desired page
//        }

       // intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/app_scoped_user_id/"+id));
        context.startActivity(intent);
*/
        Intent intent = null;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String url1 = "https://www.facebook.com/app_scoped_user_id/"+id;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href="+url1));
        } catch (Exception e) {
            // no Facebook app, revert to browser
            String url1 = "https://facebook.com/"+id;
            intent = new Intent(Intent.ACTION_VIEW);
            intent .setData(Uri.parse(url1));
        }
        context.startActivity(intent);
    }
}
