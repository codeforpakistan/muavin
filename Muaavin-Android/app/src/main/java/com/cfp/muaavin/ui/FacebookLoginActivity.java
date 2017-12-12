package com.cfp.muaavin.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.cfp.muaavin.facebook.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class FacebookLoginActivity extends ActionBarActivity  {

        TextView textview;
        Button loginButton;
        CheckBox checkBox;
        public Context context;
        String user_id;
        public static ArrayList<User> friend_list = new ArrayList<User>();
        public static User user = new User();
        public static AccessToken userToken;


        CallbackManager callbackManager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_facebook_login);
            ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
            getSupportActionBar().setBackgroundDrawable(colorDrawable);

        textview = (TextView)findViewById(R.id.tv2);
        loginButton = (Button)findViewById(R.id.login_button);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        context = FacebookLoginActivity.this;


        SharedPreferences sp = getSharedPreferences("Login", 0); // Get data from Shared Preferences...

        if(AccessToken.getCurrentAccessToken()!=null)
        {
            removeDataFromSharedPreferences();
            Intent intent = new Intent(FacebookLoginActivity.this, MenuActivity.class);
            intent.putExtra("User_signedID", /*sp.getString("UserId", "")*/String.valueOf(AccessToken.getCurrentAccessToken().getUserId()));
            startActivity(intent);
        }
        else { // if Shared Preference contains Access token
            if (!sp.getString("AccessToken", "").equals("")) {
                AccessToken accessToken = new AccessToken(sp.getString("AccessToken", ""), sp.getString("ApplicationId", ""), sp.getString("UserId", ""), sp.getStringSet("Permissions", null), sp.getStringSet("DeclinedPermissions", null), null, null, null);
                AccessToken.setCurrentAccessToken(accessToken);
                Profile profile = new Profile(sp.getString("ProfileId"  , ""), sp.getString("FirstName", ""), sp.getString("MiddleName", ""), sp.getString("LastName", ""), sp.getString("Name", ""), Uri.parse(sp.getString("Url", "")));
                Profile.setCurrentProfile(profile);
                user_id = sp.getString("UserId", "");
            }
        }
           // LoginManager.getInstance().logOut();
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   SharedPreferences sp = getSharedPreferences("Login", 0); // Get data from Shared Preferences...
                   // if Shared Preferences contains access token
                   if (AccessToken.getCurrentAccessToken()!=null/*!sp.getString("AccessToken", "").equals("")*/) {

                   if(!checkBox.isChecked()){ removeDataFromSharedPreferences(); }
                   else
                   {   SharedPreferences.Editor Ed = sp.edit();
                       Ed.putString("AccessToken", AccessToken.getCurrentAccessToken().getToken()).putString("ApplicationId", AccessToken.getCurrentAccessToken().getApplicationId()).putStringSet("Permissions", AccessToken.getCurrentAccessToken().getPermissions()).putStringSet("DeclinedPermissions", AccessToken.getCurrentAccessToken().getDeclinedPermissions()).putString("UserId", AccessToken.getCurrentAccessToken().getUserId()).putString("ProfileId", Profile.getCurrentProfile().getId()).putString("Name", Profile.getCurrentProfile().getName()).putString("Url", Profile.getCurrentProfile().getProfilePictureUri(20, 20).toString()).putString("access_expires",AccessToken.getCurrentAccessToken().getExpires().toString());
                       Ed.commit();
                   }
                         Intent intent = new Intent(FacebookLoginActivity.this, MenuActivity.class);
                         intent.putExtra("User_signedID", /*sp.getString("UserId", "")*/user_id);
                         startActivity(intent);
                   }
                   else {
                        callbackManager = CallbackManager.Factory.create();
/*
                        LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this, Arrays.asList("email", "user_status", "user_photos", "user_videos", "user_tagged_places", "user_actions.video", "user_posts", "user_friends", "public_profile", "read_page_mailboxes", "read_custom_friendlists"
));
*/
                       LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this, Arrays.asList("email", /*"user_status", "user_photos", "user_videos", "user_tagged_places", "user_actions.video",*/ "user_posts", "user_friends", "public_profile"/*, "read_page_mailboxes", "read_custom_friendlists"*/
                       ));

                        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


                        @Override
                        public void onSuccess(LoginResult loginResult) {

                           // getPublishPermissions(loginResult);
                            fetchProfile();
                            user_id = loginResult.getAccessToken().getUserId(); // 10205871243740520
                            user.id = user_id;
                            Toast.makeText(FacebookLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FacebookLoginActivity.this, MenuActivity.class);
                            intent.putExtra("User_signedID", user_id);
                            startActivity(intent);
                            }

                         @Override
                         public void onCancel() {
                             Toast.makeText(FacebookLoginActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                             }

                         @Override
                         public void onError(FacebookException exception) {
                             Toast.makeText(FacebookLoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                         } });

                        }
                }
            });
//            LoginManager.getInstance().logInWithPublishPermissions(FacebookLoginActivity.this, Arrays.asList(/*"manage_pages","publish_pages",*//*"publish_actions"*/""));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager!=null)
        callbackManager.onActivityResult(requestCode,resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }

    private void fetchProfile() {
        GraphRequest request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
            // this is where you should have the profile
            Log.v("fetched info", object.toString());
            Profile.setCurrentProfile(new Profile(object.optString("id"),object.optString("name"),object.optString("name"),object.optString("name"),object.optString("name"),Uri.parse(object.optString("link"))));//linkUri

            final AccessToken accessToken = AccessToken.getCurrentAccessToken();
            SharedPreferences sp = getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed = sp.edit();
            if(checkBox.isChecked()) {
                Ed.putString("AccessToken", accessToken.getToken()).putString("ApplicationId", accessToken.getApplicationId()).putStringSet("Permissions", accessToken.getPermissions()).putStringSet("DeclinedPermissions", accessToken.getDeclinedPermissions()).putString("UserId", accessToken.getUserId()).putString("ProfileId", Profile.getCurrentProfile().getId()).putString("Name", Profile.getCurrentProfile().getName()).putString("Url", Profile.getCurrentProfile().getProfilePictureUri(20, 20).toString()).putString("access_expires",accessToken.getExpires().toString());
                Ed.commit();
            } else{  Ed.putString("AccessToken", ""); Ed.commit();}
            } });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link"); //write the fields you need
        request.setParameters(parameters);
        request.executeAsync();
    }


    public  void removeDataFromSharedPreferences()
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

    public void getPublishPermissions(final LoginResult parseUser) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // User succesfully login with all permissions
                        // After this with these json and ParseUser , you can save your user to Parse
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException facebookException) {

            }
        });

//        , "manage_pages", "publish_pages", "pages_show_list"
        Collection<String> permissions = new ArrayList<>();
        permissions.add("manage_pages");
        permissions.add("publish_pages");
       // permissions.add("pages_show_list");
        LoginManager.getInstance().logInWithPublishPermissions(FacebookLoginActivity.this, permissions);
    }

}
