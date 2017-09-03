package com.cfp.muaavin.ui;
import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.facebook.clipboard;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.ClipBoardHelper;
import com.cfp.muaavin.helper.DataLoaderHelper;
import com.cfp.muaavin.helper.PrefManager;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.web.FriendManagement;
import com.cfp.muaavin.web.WebHttpGetReq;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

import static android.os.Environment.*;
import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;
import static com.cfp.muaavin.twitter.TwitterUtil.clearTwitterData;
import static com.cfp.muaavin.ui.TwitterLoginActivity.session;
import static com.cfp.muaavin.web.DialogBox.promptInputDialog;


public class MenuActivity extends AppCompatActivity implements UserInterface, UiUpdate, clipboard {

    public static String[] ReportPostOption = {"Notify Posts", "Notify Clipboard Post", "Notify Tweets", "Notify Group Posts"};
    static String[] HighlightUserOption = {"Highlighted Facebook Users", "Highlighted Twitter Users","Highlighted Facebook Posts", "Highlighted Twitter Posts"};
    static String[] BrowseOption = {"Browse Notified Posts", "Browse Notified Tweets", "Manage Notifications"};
    FriendManagement friend_management;
    Context contex;
    String user_id;
    public static ArrayList<User> users = new ArrayList<User>();
    DataLoaderHelper controller;
    BottomNavigationView bottomNavigationBar;
    int selectedItem;

    public static String baseURL = "http://52.176.101.55:8080/";
    private static final String TWITTER_KEY = "hVtVIbgLdTeigUj4sDy7sFTH7";//"qWbMCnZUcB9hOliWDG6IOtkNP";
    private static final String TWITTER_SECRET = "h0SGrwxv3vBg3JkGWQX8PK9SUGLkGkZjrWTlJYz3vcfqXG5MNS";//"H4KIPod4y561OXJ7u8Cd4EuGCtIofAi0HhR2hW80Ng84JgQaQ3";

    public static String pageToken = "EAADp7MxvhLcBACpQSicb7OIYSYdxt4ZB3Onhy785AnKeSF3G7XJAP47oQVQJQaTKWKLGsiDssrNIlGqpFpvGNzcytMY86RLGgAcQYr4pg8UwQOTOO2BHIZBvtsS0ExpEFbxZCHzFQharpnfouLVRxQTw40byV1w4kbYJez77AZDZD";
    public static String pageId = "123280518250126";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final String[] writeExternalStorage =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(MenuActivity.this, new Twitter(authConfig));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        setContentView(R.layout.activity_menu);

        //Fragment Mainfragment = new Fragment();
        //getFragmentManager().beginTransaction().replace(R.id.fragment_container,  Mainfragment).commit();
        bottomNavigationBar = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ImageView img = (ImageView)findViewById(R.id.imgLogo);

                switch (item.getItemId()) {
                    case R.id.Report:
                        //img.setVisibility(View.GONE);
                        selectedItem = item.getItemId();
                        DialogBox.SelectReportOption(contex, "Select Option", ReportPostOption, 1, user_id, MenuActivity.this, MenuActivity.this, false, MenuActivity.this);
                        break;

                    case R.id.HighlightUsers:
                        //img.setVisibility(View.GONE);
                        selectedItem = item.getItemId();
                        DialogBox.SelectReportOption(contex, "Select Option", HighlightUserOption, 1, user_id, MenuActivity.this, MenuActivity.this, false, MenuActivity.this);
                        break;

                    case R.id.Browse:
                        //img.setVisibility(View.GONE);
                        selectedItem = item.getItemId();
                        DialogBox.SelectReportOption(contex, "Select Option", BrowseOption, 1, user_id, MenuActivity.this, MenuActivity.this, false, MenuActivity.this);
                        break;
                }
                return true;

            }
        });

        contex = this;
        clearFacebookData();
        clearTwitterData();
        controller = new DataLoaderHelper(contex, this, MenuActivity.this);

        friend_management = new FriendManagement();
        user_id = getIntent().getStringExtra("User_signedID");
        String serverURL = null;
//      serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Users/getBlockedUsers?";
        serverURL = MenuActivity.baseURL+"Muaavin-Web/rest/Users/getBlockedUsers?";
        new WebHttpGetReq(contex, MenuActivity.this, 9, null, this).execute(serverURL);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //takeScreenshot();
            } else {
                requestPermissions(writeExternalStorage, 101);
            }
        } else {
          //  takeScreenshot();
        }
    }

    public void LogOut() {
        LoginManager.getInstance().logOut(); // Logout from Facebook
        CookieSyncManager.createInstance(this);// Logout from Twitter
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        Twitter.getSessionManager().clearActiveSession();
        session = null;
        Twitter.logOut();
    }

    @Override
    public void getReportedFriends(ArrayList<String> Friends, String dataType) {

    }

    @Override
    public void getBlockedUsers(ArrayList<String> FacebookBlockedUserIds, ArrayList<String> TwitterBlockedUserIds, HashMap<String, String> fbblockDates, HashMap<String,String> twblockDates) {

        FacebookUtil.BlockedUsersIds = FacebookBlockedUserIds;
        TwitterUtil.BlockedUserIds = TwitterBlockedUserIds;
        FacebookUtil.BlockDates = fbblockDates;
        TwitterUtil.BlockDates = twblockDates;

        user_id = String.valueOf(AccessToken.getCurrentAccessToken().getUserId());
        if (FacebookBlockedUserIds.contains(user_id)) {
            User.user_authentication = false;
            User.blockDate = FacebookUtil.BlockDates.get(user_id);
        } else {
            User.user_authentication = true;
        }
        showDialog(this);
        //ClipBoardHelper.getPostFromClipBoard(contex , user_id,MenuActivity.this );
    }

    @Override
    public void updateUi(ArrayList<User> Users, ArrayList<String> InfringingUserIds, String dataType) {
        WebServiceActivity frag = new WebServiceActivity();
        Bundle args = new Bundle();
        args.putSerializable("InfringingUsers", Users);
        args.putSerializable("InfringingUsersIds", InfringingUserIds);
        args.putString("DataType", dataType);
        frag.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        MenuItem btnIsAnonymous = menu.findItem(R.id.isAnonymous);

        if (PrefManager.getInstance(MenuActivity.this).isAnonymous())
            {
                btnIsAnonymous.setChecked(true);
            }
            else
            {
                btnIsAnonymous.setChecked(false);
            }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.menu_item_screenshot:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        takeScreenshot();
                    } else {
                        requestPermissions(writeExternalStorage, 101);
                    }
                } else {
                    takeScreenshot();
                }
                break;*/
            case R.id.isAnonymous:
                if(item.isChecked()){
                    PrefManager.getInstance(MenuActivity.this).setPrefIsAnonymous(false);
                    item.setChecked(false);
                }
                else
                {
                    PrefManager.getInstance(MenuActivity.this).setPrefIsAnonymous(true);
                    item.setChecked(true);
                }
                break;
            case R.id.rating:
                Intent intent = new Intent(MenuActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_item_new_quote:
/*
                Bundle params = new Bundle();
                params.putString("tags",User.getLoggedInUserInformation().id);
                params.putString("message", User.getLoggedInUserInformation().name+" has reported the following post");
                try {
        params.putString("attached_media[0]", new JSONObject().put("media_fbid", "1696368507338140").toString());
        params.putString("attached_media[1]", new JSONObject().put("media_fbid", "1696369530671371").toString());
        params.putString("attached_media[2]", new JSONObject().put("media_fbid", "1696369797338011").toString());
    }catch(JSONException ex){}

                new GraphRequest(
                        new AccessToken("EAADp7MxvhLcBALWHPVQfgybbA1Y24DxYlLdKBZAjwiMtQYrL0ujZCeSTdOnHESgCb3mjsc7WaZBwaqhuwyxdyEUjI2NlByWZCbCWDSmTqPCG9FRBMwZCOVzH4BDABZCwH9heVZCR3aXZCF8ciX7kqTZA5fG1DBKOqLZA2aCdmHYHFqrXITBOJwWZBZCrHZAuIt5hywPPH4jOBkeY0iwZDZD",AccessToken.getCurrentAccessToken().getApplicationId(),AccessToken.getCurrentAccessToken().getUserId(),AccessToken.getCurrentAccessToken().getPermissions(),AccessToken.getCurrentAccessToken().getDeclinedPermissions(),AccessToken.getCurrentAccessToken().getSource(),AccessToken.getCurrentAccessToken().getExpires(),AccessToken.getCurrentAccessToken().getLastRefresh()),
                        "/1692987477676243/feed",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                if(response.getError()==null)
                                    Log.i("success","true");
                                else
                                    Log.i("success","false");
                            }
                        }
                ).executeAsync();
*/

                 LogOut();
                Intent intnt = new Intent(MenuActivity.this, FacebookLoginActivity.class);
                intnt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intnt);
                break;
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else super.onBackPressed();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragment = getFragmentManager();
        if (fragment != null) {
            fragment.findFragmentByTag("TwitterFragment").onActivityResult(requestCode, resultCode, data);
        } else Log.d("Twitter", "fragment is null");
        /*if (requestCode == 1000 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
        }*/
    }

    @Override
    public void reportClipboardPost() {
        ClipBoardHelper.getPostFromClipBoard(contex, user_id, MenuActivity.this);
    }

    public void showDialog(Context context)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Muaavin");

        //String.valueOf(AccessToken.getCurrentAccessToken().getUserId()
        if(User.user_authentication == false) {
            int days = 7-Integer.parseInt(User.blockDate);
            builder.setMessage("Welcome to Muaavin!!! \n Your account is currently blocked due to some suspicious activities. It would be unblocked after "+ days + " days.");

        }            else
        builder.setMessage("Welcome to Muaavin!!! \n Enabling victims and witnesses of online harassment to instantly notify incidents to a trusted network of friends/allies.");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                if(User.user_authentication == false)
                    MenuActivity.this.finish();
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, 1000);

                    } else {
                        requestPermissions(writeExternalStorage, 101);
                    }
                } else {
                    Intent i = new Intent(
                            getApplicationContext(),UploadImages.class);
                    startActivity(i);
                }*/
                //String link = result.get(keys.get(position)).get(0).PostUrl/*.replace("_","/posts/")*/;

/*
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/accounts",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                String token = "";
                                try {

                                    JSONArray jarray = response.getJSONObject().getJSONArray("data");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject oneAlbum = jarray.getJSONObject(i);
                                        Log.i("response", oneAlbum.optString("access_token"));
                                        token = oneAlbum.optString("access_token");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("response", response.toString());

                                Bundle params = new Bundle();
                                params.putString("name", "Test Name");
                                params.putString("message", "This is a test message");
                                params.putString("link", "https://web.facebook.com/Muavin_test-123280518250126/");
                                params.putString("display", "page");
                                new GraphRequest(
                                        new AccessToken("EAADp7MxvhLcBACpQSicb7OIYSYdxt4ZB3Onhy785AnKeSF3G7XJAP47oQVQJQaTKWKLGsiDssrNIlGqpFpvGNzcytMY86RLGgAcQYr4pg8UwQOTOO2BHIZBvtsS0ExpEFbxZCHzFQharpnfouLVRxQTw40byV1w4kbYJez77AZDZD",AccessToken.getCurrentAccessToken().getApplicationId(),AccessToken.getCurrentAccessToken().getUserId(),AccessToken.getCurrentAccessToken().getPermissions(),AccessToken.getCurrentAccessToken().getDeclinedPermissions(),AccessToken.getCurrentAccessToken().getSource(),AccessToken.getCurrentAccessToken().getExpires(),AccessToken.getCurrentAccessToken().getLastRefresh()),
                                        "/123280518250126/feed",
                                        params,
                                        HttpMethod.POST,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                if(response.getError()==null)
                                                    Log.i("success","true");
                                                else
                                                    Log.i("success","false");
                                            }
                                        }
                                ).executeAsync();
                            }
                        }
                ).executeAsync();
*/

            }});
        builder.show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API. highlight
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Menu Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 101:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    takeScreenshot();
                } else {
                    requestPermissions(writeExternalStorage, 101);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String path = Environment.getExternalStorageDirectory().toString() + "/Muaavin";
            File f = new File(path);
            if(f.exists()){}
                else
            f.mkdir();

            String mPath = Environment.getExternalStorageDirectory().toString() + "/Muaavin/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


            MediaScannerConnection.scanFile(this,
                    new String[]{imageFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

            Toast.makeText(getApplicationContext(), "Screenshot saved", Toast.LENGTH_SHORT).show();

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MenuActivity.this);
            builder.setMessage("Proceed with posting?")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                          Intent intent = new Intent(getApplicationContext(),UploadImages.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do nothing
                            return;
                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();

           // openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            Toast.makeText(getApplicationContext(), "Unable to take screenshot", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}
