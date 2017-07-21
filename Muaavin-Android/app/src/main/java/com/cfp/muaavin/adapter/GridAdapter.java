package com.cfp.muaavin.adapter;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.muaavin.helper.Images;
import com.cfp.muaavin.helper.SDImageLoader;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.UploadImages;
import com.squareup.picasso.Picasso;
public class GridAdapter extends ArrayAdapter<Images> {
    private Context mContext;
    private int layoutResourceId;
    Images item;
   // int position;
    private ArrayList<Images> mGridData = new ArrayList<Images>();
    public GridAdapter(Context mContext, int layoutResourceId,ArrayList<Images> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Images> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
       row = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.chkBox = (CheckBox) row.findViewById(R.id.chkbox);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        item = mGridData.get(position);
        holder.chkBox.setChecked(item.isSelected);

        SDImageLoader mImageLoader = new SDImageLoader();

        mImageLoader.load(item.getPath(), holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScreenshot(new File(item.getPath()));
            }
        });
        holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if(isChecked) {
                    UploadImages.imageId.get(position).setSelected(true);
                    mGridData.get(position).setSelected(true);
                    UploadImages.countSelected +=1;
/*
                        Bundle params = new Bundle();
                        params.putString("caption", User.getLoggedInUserInformation().name + " has reported this post");
                        Bitmap image = BitmapFactory.decodeFile(Imageid.get(position).getPath());
                        ByteArrayOutputStream blob = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, blob);
                        byte[] bitmapdata = blob.toByteArray();
                        params.putByteArray("picture", bitmapdata);
                        //params.putString("display", "page");
                        params.putBoolean("published", false);
                        showLoading(mContext);
                        new GraphRequest(
                                new AccessToken("EAADp7MxvhLcBAPjQnwLE2Qp8QUmrcdumEpjsCD9iP1Lo6CEo2LZCM4AmO1b5ETVcSwcelFiPHY4qWBbPIwyZBjSC6L8YLZAqjgCanSb2m6laHgcwHHA2VapV84bM7YPN0MJvdvhj3D4xRQfbyDl0znoEgSkctKKouSOS4VCpbGkMFs1MbD4ucZChG6UUeWRKGM2aAjQbQQZDZD", AccessToken.getCurrentAccessToken().getApplicationId(), AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(), AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh()),
                                "/1692987477676243/photos",
                                params,
                                HttpMethod.POST,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        if (response.getError() == null) {
                                            Log.i("success", "true");
                                            String obj = response.getJSONObject().optString("id");
                                            UploadImages.imageId.get(position).setPostId(obj);
                                            UploadImages.imageId.get(position).setSelected(true);
                                        } else {
                                            UploadImages.imageId.get(position).setSelected(false);
                                            UploadImages.imageId.get(position).setPostId("");
                                            chkBox.setChecked(false);
                                        }
                                        Log.i("success", "false");
                                        hideLoading();
                                    }
                                }
                        ).executeAsync();
*/
                }
                else
                {
                    UploadImages.imageId.get(position).setSelected(false);
                    mGridData.get(position).setSelected(false);
                  //  UploadImages.imageId.get(position).setPath("");
                    UploadImages.countSelected -=1;

                }
            }
        });
        return row;
    }
    static class ViewHolder {
        CheckBox chkBox;
        ImageView imageView;
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

}