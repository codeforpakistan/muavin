package com.cfp.muaavin.BusinessLogic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.cfp.muaavin.R;
import com.cfp.muaavin.helper.AesEncryption;
import com.cfp.muaavin.helper.PostDetail;
import com.cfp.muaavin.ui.HighlightedPostsActivity;
import com.cfp.muaavin.ui.MenuActivity;
import com.cfp.muaavin.web.WebHttpGetReq;

import java.util.ArrayList;
import java.util.HashMap;

public class Highlighted_CustomAdapter extends BaseAdapter {

    HashMap<String, ArrayList<PostDetail>> result;
    ArrayList<PostDetail> Post_Details;
    Context context;
    String s;
    int[] thumbValueAlreadyset;
    boolean TwitterFeedBack;
    HighlightedPostsActivity BrowseActivityDelegate;


    private static LayoutInflater inflater=null;

    public Highlighted_CustomAdapter(Context context, HighlightedPostsActivity browser_activity, HashMap<String, ArrayList<PostDetail>> post_details) {

        result=post_details;
        this.context = context;
        BrowseActivityDelegate = browser_activity;
        thumbValueAlreadyset = new int[result.size()];

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public  interface UiUpdate
    {
        public void getPostsDetails(HashMap<String, ArrayList<PostDetail>> result);
        public void  updateDislikeButton(int position, String response);
        public void  updateFeedBack(int position, String FeedBackMessage);
        public void  postLink(String type, String postLink, String userProfile, String message, String userName);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView text_view;

        ImageView image;

        ImageView ProfilePic;

        EditText edit_text;

        Button submit ;

        TextView connectionText;

        TextView total_unlikes;

        ImageButton ThumbDownButton;
        ImageButton reportButton;

        TextView   PostHeading;

        TextView   CommentHeading;

        TextView FeedBack;

        LinearLayout linearLayout3;

        RelativeLayout title;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inflater.inflate(R.layout.browse_row_layout, null);
        final Holder holder = getHolder(rowView);//new Holder();

            ///////////
            final ArrayList<String> keys = new ArrayList<String>(result.keySet());
            String key = keys.get(position);
            Post_Details = result.get(key);
            final String post_message1 =  Post_Details .get(0).post_Detail;
            String infringingUserPic =  UrlHelper.getDecodedUrl(Post_Details .get(0).infringing_user_profile_pic);
            holder.text_view.setText(post_message1);
            if(Post_Details.get(0).IsTwitterPost)
            {
              holder.title.setBackgroundColor(Color.parseColor("#00BFFF"));
              holder.PostHeading.setBackgroundColor(Color.parseColor("#00BFFF"));
              holder.PostHeading.setText("Tweet : "+Post_Details.get(0).infringing_user_name);
              holder.FeedBack.setBackgroundColor(Color.parseColor("#87CEFA"));
                holder.reportButton.setVisibility(View.GONE);
            }
            else if(Post_Details.get(0).IsComment)
            {
                holder.PostHeading.setText("Comment : "+Post_Details.get(0).infringing_user_name);
            }
            else
            {
                holder.PostHeading.setText("Post : "+Post_Details.get(0).infringing_user_name);
            }
            new ImageSelectorAsyncTask(holder.ProfilePic, holder.connectionText).execute(infringingUserPic);

            String image = UrlHelper.getDecodedUrl(Post_Details.get(0).post_image);

            holder.CommentHeading.setVisibility(View.GONE);

            int i = 0;

            for(int j = 0 ; j < Post_Details.get(0).FeedBacks.size(); j++)
            {
/*
                final RelativeLayout relative_layout = new RelativeLayout(context);
                ImageView PersonImage = getImageView();
                PersonImage.setImageResource(R.drawable.single_person_icon);
                final TextView TextViewFeedBack = getRowTextView(Post_Details.get(0).FeedBacks.get(j) , i);
                holder.linearLayout3.addView(relative_layout);
                relative_layout.addView(TextViewFeedBack);
                relative_layout.addView(PersonImage);
*/
                final LinearLayout relative_layout = new LinearLayout(context);
                relative_layout.setOrientation(LinearLayout.HORIZONTAL);
                ImageView PersonImage = getImageView();
                PersonImage.setImageResource(R.drawable.single_person_icon);
                final TextView TextViewFeedBack = getRowTextViewFeedback(Post_Details.get(0).FeedBacks.get(j) , i);
                holder.linearLayout3.addView(relative_layout);
                relative_layout.addView(PersonImage);
                relative_layout.addView(TextViewFeedBack);
                holder.linearLayout3.setTag(Post_Details.get(0).FeedBacks.get(j));
                TextViewFeedBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure to delete the record?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String comment = TextViewFeedBack.getText().toString();//(String)v.getTag();
                                        String serverURL = null;

                                        try {
                                            if(result.get(keys.get(position)).get(0).IsTwitterPost) { TwitterFeedBack = true; }
                                            //      serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/FeedBack/Add_FeedBack?user_id=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) +"&InfringingUserId="+AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+ "&post_id=" + AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id )+ "&comment=" +AesEncryption.encrypt( comment)+"&IsTwitterFeedBack="+TwitterFeedBack+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                                            serverURL = Admin.baseURL+"Muaavin-Web/rest/FeedBack/Delete_FeedBack?InfringingUserId="+ AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+ "&post_id=" + AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id )+ "&comment=" + AesEncryption.encrypt( comment)+"&IsTwitterFeedBack="+TwitterFeedBack+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                                        } catch (Exception e) { e.printStackTrace(); }
                                        new WebHttpGetReq(context, 10, position,BrowseActivityDelegate).execute(serverURL);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do nothing

                                        return;
                                    }
                                })
                        ;
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
            }


            new ImageSelectorAsyncTask(holder.image, holder.text_view).execute(image);

            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = holder.edit_text.getText().toString();
                    String serverURL = null;

/*
                try {
                      if(result.get(keys.get(position)).get(0).IsTwitterPost) { TwitterFeedBack = true; }
                //      serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/FeedBack/Add_FeedBack?user_id=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) +"&InfringingUserId="+AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+ "&post_id=" + AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id )+ "&comment=" +AesEncryption.encrypt( comment)+"&IsTwitterFeedBack="+TwitterFeedBack+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                    serverURL = Admin.baseURL+"Muaavin-Web/rest/FeedBack/Add_FeedBack?user_id=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) +"&InfringingUserId="+ AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+ "&post_id=" + AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id )+ "&comment=" + AesEncryption.encrypt( comment)+"&IsTwitterFeedBack="+TwitterFeedBack+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                } catch (Exception e) { e.printStackTrace(); }
              //      new WebHttpGetReq(context, 10, holder.text_view, position,null,BrowseActivityDelegate).execute(serverURL);
*/
                }
            });

            holder.PostHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UrlHelper.showDataOnBrowser(context , result.get(keys.get(position)).get(0).PostUrl);
                }
            });


        holder.reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        holder.total_unlikes.setText(String.valueOf(result.get(keys.get(position)).get(0).unlike_value));
        if((float)result.get(keys.get(position)).get(0).unlike_value > (float)(result.get(keys.get(position)).get(0).count/10))
            holder.PostHeading.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));

            holder.ThumbDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {

                String serverURL = null;
       /*         try {
                  if(result.get(keys.get(position)).get(0).IsTwitterPost) { TwitterFeedBack = true; }
               //   serverURL = "http://13.76.175.64:8080/Muaavin-Web/rest/ThumbsDown/Add_ThumbsDown?user_id="+ AesEncryption.encrypt(Profile.getCurrentProfile().getId())+"&InfringingUserId="+AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+"&post_id="+AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id)+"&IsTwitterPost="+result.get(keys.get(position)).get(0).IsTwitterPost+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                    serverURL = Admin.baseURL+"Muaavin-Web/rest/ThumbsDown/Add_ThumbsDown?user_id="+ AesEncryption.encrypt(Profile.getCurrentProfile().getId())+"&InfringingUserId="+ AesEncryption.encrypt(result.get(keys.get(position)).get(0).infringing_user_id)+"&post_id="+ AesEncryption.encrypt(result.get(keys.get(position)).get(0).post_id)+"&IsTwitterPost="+result.get(keys.get(position)).get(0).IsTwitterPost+"&IsComment="+result.get(keys.get(position)).get(0).IsComment;
                } catch (Exception e) { e.printStackTrace(); }

                new WebHttpGetReq(context, 3,holder.total_unlikes,position,null,BrowseActivityDelegate).execute(serverURL);
      */      }
        });



        return rowView;
    }

    public Holder getHolder(View rowView )
    {
        Holder holder = new Holder();
        holder.text_view=(TextView) rowView.findViewById(R.id.Textbox1);
        holder.image=(ImageView) rowView.findViewById(R.id.Image_view);
        holder.edit_text = (EditText) rowView.findViewById(R.id.edit_text1);
        holder.submit = (Button)rowView.findViewById(R.id.submit);
        holder.connectionText =(TextView)rowView.findViewById(R.id.Textbox2);
        holder.total_unlikes = (TextView)rowView.findViewById(R.id.total_unlikes);
        holder.ThumbDownButton = (ImageButton)rowView.findViewById(R.id.image_button);
        holder.reportButton = (ImageButton)rowView.findViewById(R.id.report);
        holder.PostHeading = (TextView)rowView.findViewById(R.id.Textbox2);
        holder.CommentHeading = (TextView)rowView.findViewById(R.id.Textbox3);
        holder.linearLayout3 = (LinearLayout)rowView.findViewById(R.id.linear3);
        holder.ProfilePic = (ImageView) rowView.findViewById(R.id.ProfilePic);
        holder.FeedBack = (TextView) rowView.findViewById(R.id.FeedBack);
        holder.title = (RelativeLayout) rowView.findViewById(R.id.title);

        return holder;
    }

    /// Get Text View
    public TextView getRowTextView( String text , int id)
    {
        TextView rowTextView = new TextView(context);
        rowTextView.setId(id);
        rowTextView.setText(text);
        rowTextView.setTextSize(15);
        rowTextView.setLayoutParams(getLinearLayoutParams());
        rowTextView.setBackgroundColor(Color.parseColor("#dfe3ee"));

        return rowTextView;

    }

    public TextView getRowTextViewFeedback( String text , int id)
    {
        TextView rowTextView = new TextView(context);
        rowTextView.setId(id);
        rowTextView.setText(text);
        rowTextView.setTextSize(15);
        rowTextView.setLayoutParams(getLinearLayoutParamsFeedback());
        rowTextView.setBackgroundColor(Color.parseColor("#dfe3ee"));

        return rowTextView;

    }

    // Get Layout  for Text View
    public LinearLayout.LayoutParams getLinearLayoutParams()
    {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(100, 0, 20, 20);

        return params;

    }

    public LinearLayout.LayoutParams getLinearLayoutParamsFeedback()
    {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 0, 20, 20);

        return params;

    }

    public ImageView getImageView()
    {
        ImageView image_view = new ImageView(context);
        image_view.setLayoutParams(getRelativeLayoutParams());
        return image_view;
    }

    // Get Layout for Image View
    public RelativeLayout.LayoutParams getRelativeLayoutParams()
    {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        return params;
    }

  /*  public void showDialog(final Context context, final String link, final String userProfile, final String message, final String userName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Share as");
        final String[] category = new String[]{"Link Posting", "Photo Posting"};
        builder.setItems(category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (User.user_authentication == false) {  return; }

                if(category[which].equals("Link Posting")) {
                    BrowseActivityDelegate.postLink("Link Posting",link,userProfile,message, userName);
                }

                if(category[which].equals("Photo Posting")) {
                    BrowseActivityDelegate.postLink("Photo Posting",link,userProfile,message, userName);
                }
            }
        });
        builder.show();
    }*/
}
