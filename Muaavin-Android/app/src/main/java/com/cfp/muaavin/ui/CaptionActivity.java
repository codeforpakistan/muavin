package com.cfp.muaavin.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cfp.muaavin.adapter.CaptionGridAdapter;
import com.cfp.muaavin.adapter.GridAdapter;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.helper.Images;
import com.cfp.muaavin.helper.border;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CaptionActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    GridView grid;
    //Button btnUpload;
    CaptionGridAdapter adapter = null;
    ProgressDialog progressDialog = null;
    //public List<Images> imageId = null;
    public static ArrayList<Images> images = null;
    File[] listFile = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static int countSelected = 0;
    public static int countUploaded = 0;
    //ArrayList<String> f = null;
    String fileName="";
    List<ImageView> imgView = new ArrayList<ImageView>();
    List<EditText> etView = new ArrayList<EditText>();
    String post_url,user_profile,message,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            fileName = bundle.getString("file_name");
            post_url = bundle.getString("post_url");
            user_profile = bundle.getString("user_profile");
            message = bundle.getString("message");
            userName = bundle.getString("user_name");
        }

        countSelected = 0;
        countUploaded = 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3b5998"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        grid = (GridView) findViewById(R.id.grid);

        LinearLayout li = (LinearLayout)findViewById(R.id.li);

       // imageId =  (ArrayList<Images>)getIntent().getParcelableArrayListExtra("list");
        images = new ArrayList<Images>();
        for ( int i = 0; i < UploadImages.imageId.size(); i++) {
            if (UploadImages.imageId.get(i).isSelected) {
                images.add(UploadImages.imageId.get(i));
/*
                ImageView myImage = new ImageView(this);
                myImage.setImageBitmap(BitmapFactory.decodeFile(images.get(i).getPath()));
                li.addView(myImage);
                EditText et1 = new EditText(this);
                li.addView(et1);
*/

            }
        }
        for ( int i = 0; i < images.size(); i++) {
                ImageView myImage = new ImageView(this);
                myImage.setImageBitmap(BitmapFactory.decodeFile(images.get(i).getPath()));
                LayoutParams params = new LayoutParams(250, 500);
                params.gravity= Gravity.CENTER_HORIZONTAL;
                myImage.setLayoutParams(params);
                myImage.setId(i);
                imgView.add(myImage);
            li.addView(myImage);
            LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                EditText et1 = new EditText(this);
            params1.setMargins(30,10,30,10);
            et1.setLayoutParams(params1);
            et1.setHint("caption");
            et1.setId(i);
            etView.add(et1);
            li.addView(et1);
        }

        adapter = new CaptionGridAdapter(CaptionActivity.this, null, images);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                openScreenshot(new File(images.get(position).getPath()));
                Toast.makeText(CaptionActivity.this, "You Clicked at " + position, Toast.LENGTH_SHORT).show();

            }
        });

          /*  btnBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                openImageChooser();
                }
            });*/

         /*   btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadImages();
                }
            });*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onResume() {
        super.onResume();
    //    populateGrid();

    }

    /* Choose an image from Gallery */
    void uploadImages(final String postCaption) {

        showLoading(CaptionActivity.this);
        countSelected = images.size();
        for ( int i = 0; i < images.size(); i++) {
            final int count = i;

                Bundle params = new Bundle();
                String caption = etView.get(i).getText().toString();
                if(caption==null||caption.equals(""))
                params.putString("caption", User.getLoggedInUserInformation().name + " has reported this post");
                else
                params.putString("caption", caption);

                Bitmap image = BitmapFactory.decodeFile(images.get(i).getPath());
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, blob);
                byte[] bitmapdata = blob.toByteArray();
                params.putByteArray("picture", bitmapdata);
                //params.putString("link","https://web.facebook.com/photo.php?fbid=1994652610560424&set=a.769130273112670.1073741829.100000471134793&type=3&theater");
                //params.putString("display", "page");
                params.putBoolean("published", false);
                //showLoading(mContext);
                new GraphRequest(
                        new AccessToken(MenuActivity.pageToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(), AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh()),
                        "/"+MenuActivity.pageId+"/photos",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                if (response.getError() == null) {
                                    countUploaded +=1;
                                    Log.i("success", "true");
                                    String obj = response.getJSONObject().optString("id");
                                    images.get(count).setPostId(obj);
                                    images.get(count).setSelected(true);

                                    if(countUploaded==countSelected)
                                    {
                                        uploadAlbum(postCaption);
                                    }
                                } else {
                                    images.get(count).setSelected(false);
                                    images.get(count).setPostId("");
                                    //chkBox.setChecked(false);
                                }
//                                Log.i("success", "false");
                            }
                        }
                ).executeAsync();

        }
    }

    public void uploadAlbum(final String caption)
    {
        Bundle params = new Bundle();
        params.putString("tags", User.getLoggedInUserInformation().id);

        if(caption==null||caption.equals(""))
            params.putString("message", User.getLoggedInUserInformation().name +" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following: \n Offender Details -> "+userName+" ( https://web.facebook.com/"+user_profile+" ) "+" \n Comment -> "+message+"\n For further details please use the following link: \n "+post_url);
        else
        params.putString("message", User.getLoggedInUserInformation().name +" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following:\n"+
                caption+"\n" + "Offender Details -> "+userName+" ( https://web.facebook.com/"+user_profile+" ) "+" \n Comment -> "+message+"\n For further details please use the following link: \n "+post_url);


  /*      if(caption==null||caption.equals(""))
            params.putString("message", User.getLoggedInUserInformation().name + " has reported the following post");
        else
            params.putString("message", caption);
  */      try {
            int counter = 0;
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).isSelected) {
                    params.putString("attached_media[" + counter + "]", new JSONObject().put("media_fbid", images.get(i).getPostId()).toString());
                    counter++;
                }
            }
            if(counter>0){
                new GraphRequest(
                        new AccessToken(MenuActivity.pageToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(), AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh()),
                        "/"+MenuActivity.pageId+"/feed",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                if (response.getError() == null)
                                {hideLoading();
                                    Toast.makeText(getApplicationContext(), "Photos reported successfully", Toast.LENGTH_LONG).show();
                                    CaptionActivity.this.finish();
                                }
                                else {
                                    Bundle params = new Bundle();
                                    params.putString("tags", User.getLoggedInUserInformation().id);
/*
                                    if(caption==null||caption.equals(""))
                                        params.putString("message", User.getLoggedInUserInformation().name + " has reported the following: \n Offender Details -> "+userName+" ( https://web.facebook.com/"+user_profile+" ) "+" \n Comment -> "+message);
                                    else
                                        params.putString("message", caption+"\n" + "Offender Details -> "+userName+" ( https://web.facebook.com/"+user_profile+" ) "+" \n Comment -> "+message);
*/
                                    if(caption==null||caption.equals(""))
                                        params.putString("message", User.getLoggedInUserInformation().name +" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following: \n Offender Details -> "+userName+" ( https://web.facebook.com/"+user_profile+" ) "+" \n Comment -> "+message);
                                    else
                                        params.putString("message", User.getLoggedInUserInformation().name +" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following:\n"+
                                                caption+"\n" + "Offender Details -> "+userName+" ( https://web.facebook.com/"+user_profile+" ) "+" \n Comment -> "+message);

                                    /*

                                    if(caption==null||caption.equals(""))
                                        params.putString("message", User.getLoggedInUserInformation().name + " has reported the following post");
                                    else
                                        params.putString("message", caption);*/
                                    try {
                                        int counter = 0;
                                        for (int i = 0; i < images.size(); i++) {
                                            if (images.get(i).isSelected) {
                                                params.putString("attached_media[" + counter + "]", new JSONObject().put("media_fbid", images.get(i).getPostId()).toString());
                                                counter++;
                                            }
                                        }
                                        if(counter>0){
                                            new GraphRequest(
                                                    new AccessToken(MenuActivity.pageToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(), AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh()),
                                                    "/"+MenuActivity.pageId+"/feed",
                                                    params,
                                                    HttpMethod.POST,
                                                    new GraphRequest.Callback() {
                                                        public void onCompleted(GraphResponse response) {
                                                            hideLoading();
                                                            if (response.getError() == null){
                                                                Toast.makeText(getApplicationContext(), "Photos reported successfully", Toast.LENGTH_LONG).show();
                                                            CaptionActivity.this.finish();
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(), "Unable to report photos, Please try again.", Toast.LENGTH_LONG).show();
                                                            }

                                                        }
                                                    }
                                            ).executeAsync();
                                        }else {
                                            hideLoading();
                                            Toast.makeText(getApplicationContext(), "No photos available to report, Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException ex) {
                                    }  }

                            }
                        }
                ).executeAsync();
            }else {
                hideLoading();
                Toast.makeText(getApplicationContext(), "No photos available to report, Please try again.", Toast.LENGTH_LONG).show();
                CaptionActivity.this.finish();
            }
        } catch (JSONException ex) {
        }
    }

    void deleteImages() {

        try {
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).isSelected) {
                    File file = new File(images.get(i).getPath());
                    file.delete();
                }
            }

            populateGrid();
        } catch (Exception ex) {
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    //     imageId.add(selectedImageUri);
                    adapter.notifyDataSetChanged();
//                    imgView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

   /* @Override
    public void onClick(View v) {
        openImageChooser();
    }*/

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.caption, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_upload:
                showDialog();
                break;
          /*  case R.id.menu_item_delete:
                deleteImages();
                break;*/
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

    public void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CaptionActivity.this);
        builder.setTitle("Add caption...");
        final EditText input = new EditText(CaptionActivity.this);
        input.setSingleLine(false);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(3);
        input.setMaxLines(10);
        input.setVerticalScrollBarEnabled(true);
        input.setMovementMethod(ScrollingMovementMethod.getInstance());
        input.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        FrameLayout container = new FrameLayout(CaptionActivity.this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 30;
        params.rightMargin = 30;
        params.topMargin = 10;
        params.bottomMargin = 10;
        input.setBackgroundDrawable(new border(0xff999999, 10));
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadImages(input.getText().toString());
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                uploadImages("");
                dialog.cancel();
            }
        });

        builder.show();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("UploadImages Page") // TODO: Define a title for the content shown.
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



    public void showLoading(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable
                (new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(new ProgressBar(context));
    }

    public void hideLoading() {
        progressDialog.setCancelable(true);
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void populateGrid() {
        images = new ArrayList<Images>();
        File file = new File(Environment.getExternalStorageDirectory(), "Muaavin");
        if (file.exists() && file.isDirectory()) {
            listFile = file.listFiles();
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if(fileName.equals(listFile[i].getName()))
                        images.add(new Images(listFile[i].getAbsolutePath(), true, ""));
                    else
                        images.add(new Images(listFile[i].getAbsolutePath(), false, ""));
                }
                adapter = new CaptionGridAdapter(CaptionActivity.this, null, images);
                grid.setAdapter(adapter);
                //  adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Please take screenshots from app. and try again", Toast.LENGTH_LONG).show();
                this.finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please take screenshots from app. and try again", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }
}