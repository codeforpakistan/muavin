package com.cfp.muaavin.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cfp.muaavin.facebook.AsyncResponsePostsDet;
import com.cfp.muaavin.adapter.Browser_CustomAdapter;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.facebook.PostDetail;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.border;
import com.cfp.muaavin.web.WebHttpGetReq;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



public class Browse_Activity extends Fragment implements AsyncResponsePostsDet,Browser_CustomAdapter.UiUpdate {

    public Context context;
    ListView list_view;
    String Group_name;
    HashMap<String, ArrayList<PostDetail>> PostDetails;
    Boolean isFb = false;
    ProgressDialog progressDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view  = inflater.inflate(R.layout.browse_layout, container, false);
        context = getActivity();
        list_view = (ListView)view.findViewById(R.id.listView1);
        Group_name = getArguments().getString("Group_name");
        isFb = getArguments().getBoolean("isFb");

        String serverURL = null;
        try {

//          serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/Posts_Query/GetPosts?name="+ AesEncryption.encrypt(Group_name);
            serverURL = MenuActivity.baseURL+"Muaavin-Web/rest/Posts_Query/GetPosts?name="+ AesEncryption.encrypt(Group_name);
        } catch (Exception e) { e.printStackTrace(); }

        if(isFb)
        new WebHttpGetReq(context, getActivity(), 2,this).execute(serverURL);
        else
           new WebHttpGetReq(context, getActivity(), 333,this).execute(serverURL);

        return  view;
    }


    @Override
    public void getPostsDetails(HashMap<String, ArrayList<PostDetail>> result) {

        PostDetails = result;
        Browser_CustomAdapter c = new Browser_CustomAdapter(context,this, PostDetails);
        list_view.setAdapter(c);
    }

    @Override
    public void updateDislikeButton(int position, String response)
    {
        if(response.equals("Record Deleted"))
        {
            final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
            PostDetails.get(keys.get(position)).get(0).unlike_value--;
        }
        else
        {
            final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
            PostDetails.get(keys.get(position)).get(0).unlike_value++;
        }
        ((BaseAdapter) list_view.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void updateFeedBack(int position,String comment) {
        final ArrayList<String> keys = new ArrayList<String>(PostDetails.keySet());
        PostDetails.get(keys.get(position)).get(0).FeedBacks.add(comment);
        ((BaseAdapter) list_view.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void postLink(String type, String postLink, String userProfile, String message, String userName) {


        if(type.equals("Link Posting")) {

            takeScreenshot(type,postLink,userProfile,message,userName);
        }

        if(type.equals("Photo Posting")) {
            takeScreenshot(type,postLink,userProfile,message,userName);
        }
    }

    private void takeScreenshot(String type, final String post, final String user, final String message, final String userName) {
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
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            final File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            if(type.equals("Link Posting"))
            showDialog(bitmap, post,user, message, userName);

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            outputStream.flush();
            outputStream.close();


            MediaScannerConnection.scanFile(context,
                    new String[]{imageFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

     //       Toast.makeText(context, "Screenshot saved", Toast.LENGTH_SHORT).show();


            if(type.equals("Photo Posting")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setMessage("Proceed with posting?")
                        .setCancelable(false)
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(context, UploadImages.class);
                                intent.putExtra("file_name",imageFile.getName());
                                intent.putExtra("post_url",post);
                                intent.putExtra("user_profile",user);
                                intent.putExtra("message",message);
                                intent.putExtra("user_name",userName);

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
            }
            // openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            Toast.makeText(context, "Unable to take screenshot", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void showDialog(final Bitmap bitmap, final String post, final String user, final String message, final String userName){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add caption...");
        final EditText input = new EditText(context);
        input.setSingleLine(false);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(3);
        input.setMaxLines(10);
        input.setVerticalScrollBarEnabled(true);
        input.setMovementMethod(ScrollingMovementMethod.getInstance());
        input.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        FrameLayout container = new FrameLayout(context);
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
                postPostLink(bitmap, post,user,input.getText().toString(), message, userName);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
          //      postPostLink(bitmap, post,user,"", message, userName);
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void postPostLink(final Bitmap bitmap, String post, final String user, final String caption, final String message, final String userName){

        showLoading(context);
        Bundle params = new Bundle();
        if(caption.equals("")||caption==null)
        params.putString("caption", User.getLoggedInUserInformation().name+" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following: \n Offender Details -> "+userName+" ( https://web.facebook.com/"+user+" ) "+" \n Comment -> "+message+"\n For further details please use the following link: \n "+post);
        else
        params.putString("caption", User.getLoggedInUserInformation().name+" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following:\n"+
                caption+"\n" + "Offender Details -> "+userName+" ( https://web.facebook.com/"+user+" ) "+" \n Comment -> "+message+"\n For further details please use the following link: \n "+post);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, blob);
        byte[] bitmapdata = blob.toByteArray();
        params.putByteArray("picture", bitmapdata);
        new GraphRequest(
                new AccessToken(MenuActivity.pageToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(), AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh()),
                "/"+ MenuActivity.pageId+"/photos",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() == null) {
                            hideLoading();
                            Toast.makeText(context, "Post reported successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Bundle params = new Bundle();
                            if(caption.equals("")||caption==null)
                                params.putString("caption", User.getLoggedInUserInformation().name+" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following: \n Offender Details -> "+userName+" ( https://web.facebook.com/"+user+" ) "+" \n Comment -> "+message);
                            else
                                params.putString("caption", User.getLoggedInUserInformation().name+" ( https://web.facebook.com/"+User.getLoggedInUserInformation().id+ " ) has reported the following:\n"+
                                        caption+"\n" + "Offender Details -> "+userName+" ( https://web.facebook.com/"+user+" ) "+" \n Comment -> "+message);

                            ByteArrayOutputStream blob = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, blob);
                            byte[] bitmapdata = blob.toByteArray();
                            params.putByteArray("picture", bitmapdata);

                            new GraphRequest(
                                    new AccessToken(MenuActivity.pageToken, AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(), AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh()),
                                    "/"+MenuActivity.pageId+"/photos",
                                    params,
                                    HttpMethod.POST,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
                                            if (response.getError() == null) {
                                                hideLoading();
                                                Toast.makeText(context, "Post reported successfully", Toast.LENGTH_LONG).show();
                                            } else {
                                                hideLoading();
                                                Toast.makeText(context, "Unable to report Post, Please try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                            ).executeAsync();
                        }
                    }
                }
        ).executeAsync();
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
}
