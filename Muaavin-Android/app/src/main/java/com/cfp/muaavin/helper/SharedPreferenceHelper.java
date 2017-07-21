package com.cfp.muaavin.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tooba Saeed on 16/03/2017.
 */

public class SharedPreferenceHelper {

    public static void removeDataFromSharedPreferences(Context context)
    {
        SharedPreferences mySPrefs = context.getSharedPreferences("Login",0);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("AccessToken");
        editor.remove("ApplicationId");
        editor.remove("UserId");
        editor.remove("Permissions");
        editor.remove("DeclinedPermissions");
        mySPrefs.edit().clear().apply();
        editor.apply();
    }
}
