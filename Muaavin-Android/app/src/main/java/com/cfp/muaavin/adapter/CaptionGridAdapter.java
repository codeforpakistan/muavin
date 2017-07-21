package com.cfp.muaavin.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cfp.muaavin.helper.Images;
import com.cfp.muaavin.ui.CaptionActivity;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.UploadImages;

import java.util.List;

public class CaptionGridAdapter extends BaseAdapter{
    private Context mContext;
    //private final String[] web;
    private final List<Images> Imageid;
    ProgressDialog progressDialog = null;
    public CaptionGridAdapter(Context c, String[] web, List<Images> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
      //  this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = null;
        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.caption_layout, null);
            EditText input = (EditText) grid.findViewById(R.id.caption);
            input.setFocusable(true);
            input.setEnabled(true);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);

            imageView.setImageBitmap(BitmapFactory.decodeFile(Imageid.get(position).getPath()));

           /* input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus)
                        CaptionActivity.images.get(position).setPostId(input.getText().toString());

                }
            });*/
         /*   input.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        CaptionActivity.images.get(position).setPostId(input.getText().toString());
                        return true;
                    }
                    return false;
                }
            });*/
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    public void showLoading(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable
                (new ColorDrawable(android.graphics.Color.TRANSPARENT));
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