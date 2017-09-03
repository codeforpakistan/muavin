package com.cfp.muaavin.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cfp.muaavin.adapter.GridAdapter;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.helper.Images;
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

public class UploadImages extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    GridView grid;
    //Button btnUpload;
    GridAdapter adapter = null;
    ProgressDialog progressDialog = null;
    public static ArrayList<Images> imageId = null;
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
    String post_url,user_profile,message,userName;
    int check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            fileName = bundle.getString("file_name");
            post_url = bundle.getString("post_url");
            user_profile = bundle.getString("user_profile");
            message = bundle.getString("message");
            userName = bundle.getString("user_name");
            check = bundle.getInt("check");
        }
        countSelected = 0;
        countSelected = 0;
        countUploaded = 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        grid = (GridView) findViewById(R.id.grid);

        imageId = new ArrayList<Images>();
        File file = new File(Environment.getExternalStorageDirectory(), "Muaavin");
        if (file.exists() && file.isDirectory()) {
            listFile = file.listFiles();
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if(fileName.equals(listFile[i].getName())) {
                        imageId.add(new Images(listFile[i].getAbsolutePath(), true, ""));
                        countSelected+=1;
                    }
                    else
                        imageId.add(new Images(listFile[i].getAbsolutePath(), false, ""));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please take screenshots from app. and try again", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please take screenshots from app. and try again", Toast.LENGTH_LONG).show();
        }

        adapter = new GridAdapter(UploadImages.this, R.layout.row_layout, imageId);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

           /*     openScreenshot(new File(imageId.get(position).getPath()));
                Toast.makeText(UploadImages.this, "You Clicked at " + position, Toast.LENGTH_SHORT).show();
*/
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
        //populateGrid();

    }

    /* Choose an image from Gallery */
    void uploadImages() {


        showLoading(UploadImages.this);
        for ( int i = 0; i < imageId.size(); i++) {
            final int count = i;
            if (imageId.get(i).isSelected) {

                Bundle params = new Bundle();
                params.putString("caption", User.getLoggedInUserInformation().name + " has notified this post");
                Bitmap image = BitmapFactory.decodeFile(imageId.get(i).getPath());
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, blob);
                byte[] bitmapdata = blob.toByteArray();
                params.putByteArray("picture", bitmapdata);
                params.putString("link","https://web.facebook.com/photo.php?fbid=1994652610560424&set=a.769130273112670.1073741829.100000471134793&type=3&theater");
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
                                    imageId.get(count).setPostId(obj);
                                    imageId.get(count).setSelected(true);

                                    if(countUploaded==countSelected)
                                    {
                                        uploadAlbum();
                                    }
                                } else {
                                    imageId.get(count).setSelected(false);
                                    imageId.get(count).setPostId("");
                                    //chkBox.setChecked(false);
                                }
//                                Log.i("success", "false");
                            }
                        }
                ).executeAsync();
            }
        }
    }

    public void uploadAlbum()
    {
        Bundle params = new Bundle();
        params.putString("tags", User.getLoggedInUserInformation().id);
        params.putString("message", User.getLoggedInUserInformation().name + " has notified the following post");
        try {
            int counter = 0;
            for (int i = 0; i < imageId.size(); i++) {
                if (imageId.get(i).isSelected) {
                    params.putString("attached_media[" + counter + "]", new JSONObject().put("media_fbid", imageId.get(i).getPostId()).toString());
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
                                if (response.getError() == null)
                                    Toast.makeText(getApplicationContext(), "Photos notified successfully", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Unable to notify photos, Please try again.", Toast.LENGTH_LONG).show();

                            }
                        }
                ).executeAsync();}else {
                hideLoading();
                Toast.makeText(getApplicationContext(), "No photos available to notify, Please try again.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException ex) {
        }}

    void deleteImages() {

        try {
            for (int i = 0; i < imageId.size(); i++) {
                if (imageId.get(i).isSelected) {
                    File file = new File(imageId.get(i).getPath());
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
        inflater.inflate(R.menu.upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_upload:
//                uploadImages();
                fileName = "";
                Intent intent = new Intent(getApplicationContext(),CaptionActivity.class);
                intent.putExtra("post_url",post_url);
                intent.putExtra("user_profile",user_profile);
                intent.putExtra("message",message);
                intent.putExtra("user_name",userName);
                intent.putExtra("check",check);
                //intent.putParcelableArrayListExtra("list",images);
                startActivity(intent);
                UploadImages.this.finish();
                break;
            case R.id.menu_item_delete:
                deleteImages();
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
        imageId = new ArrayList<Images>();
        File file = new File(Environment.getExternalStorageDirectory(), "Muaavin");
        if (file.exists() && file.isDirectory()) {
            listFile = file.listFiles();
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if(fileName.equals(listFile[i].getName()))
                        imageId.add(new Images(listFile[i].getAbsolutePath(), true, ""));
                    else
                        imageId.add(new Images(listFile[i].getAbsolutePath(), false, ""));
                }
                adapter = new GridAdapter(UploadImages.this, R.layout.row_layout, imageId);
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