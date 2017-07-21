package com.cfp.muaavin.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.helper.DataLoaderHelper;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import com.twitter.sdk.android.core.*;
import java.util.ArrayList;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TwitterLoginActivity extends Fragment implements UiUpdate{

    private TwitterLoginButton loginButton;
    public static TwitterSession session = null;
    Context context ;
    String option;
    String[] group = new String[]{"Sexual harassment", "Incitement to violence","Trans rights","All of the above"};
    public DataLoaderHelper controller;


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "hVtVIbgLdTeigUj4sDy7sFTH7";
    private static final String TWITTER_SECRET = "h0SGrwxv3vBg3JkGWQX8PK9SUGLkGkZjrWTlJYz3vcfqXG5MNS";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.twitter_login_screen, container, false);
        context = getActivity();
        option = getArguments().getString("option");
        controller = new DataLoaderHelper(context,this,getActivity());

        loginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                session = Twitter.getSessionManager().getActiveSession();
                final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                final OkHttpClient customClient = new OkHttpClient.Builder() .addInterceptor(loggingInterceptor).build();

                // pass custom OkHttpClient into TwitterApiClient and add to TwitterCore
                final TwitterApiClient customApiClient;
                if (session != null) {
                    customApiClient = new TwitterApiClient(session, customClient);
                    TwitterCore.getInstance().addApiClient(session, customApiClient);
                } else {
                    customApiClient = new TwitterApiClient(customClient);
                    TwitterCore.getInstance().addGuestApiClient(customApiClient);
                }

                controller.loadTwitterData("LoadUser");
                controller.loadTwitterData(option);
               // String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
               // Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void updateUi(ArrayList<User> Users, ArrayList<String> InfringingUserIds, String dataType) {
        WebServiceActivity frag = new WebServiceActivity(); Bundle args = new Bundle();
        args.putSerializable("InfringingUsers", Users); args.putSerializable("InfringingUsersIds", InfringingUserIds); args.putString("DataType", dataType);
        frag.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
    }
}






