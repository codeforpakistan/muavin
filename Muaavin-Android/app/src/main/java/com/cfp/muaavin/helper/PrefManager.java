package com.cfp.muaavin.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    /************************ String Types **********************/

    private String prefUserPresent = "isAuthenticated";
    private String prefIsAnonymous = "isAnonymous";

    public boolean isUserAuthenticated() {
        System.out.println("Preference Get " + prefUserPresent + " = " + mPref.getBoolean(prefUserPresent, false));
        return mPref.getBoolean(prefUserPresent, false);
    }

    public void setUserAuthentication(boolean value) {

        mPref.edit().putBoolean(prefUserPresent, value).commit();
        System.out.println("Preference Set " + prefUserPresent + " = " + isUserAuthenticated());
    }

    public boolean isAnonymous() {
        System.out.println("Preference Get " + prefIsAnonymous + " = " + mPref.getBoolean(prefIsAnonymous, false));
        return mPref.getBoolean(prefIsAnonymous, false);
    }

    public void setPrefIsAnonymous(boolean value) {

        mPref.edit().putBoolean(prefIsAnonymous, value).commit();
        System.out.println("Preference Set " + prefIsAnonymous + " = " + isAnonymous());
    }

    /************************ ------ **********************/

    private static final String PREF_NAME = "com.cfp.muaavin";

    private static PrefManager instance;
    private final SharedPreferences mPref;

    private PrefManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new PrefManager(context);
        }
    }

    public static synchronized PrefManager getInstance(Context ctxt) {
        if (instance == null) {
           /* throw new IllegalStateException(PrefManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
           */
            instance = new PrefManager(ctxt);
        }
        return instance;
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }


}
