package com.cfp.muaavin.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.facebook.clipboard;
import com.cfp.muaavin.helper.DataLoaderHelper;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.TwitterLoginActivity;
import com.cfp.muaavin.ui.UiUpdate;
import com.twitter.sdk.android.core.models.Tweet;
import static com.cfp.muaavin.facebook.FacebookUtil.clearFacebookData;
import static com.cfp.muaavin.twitter.TwitterUtil.clearTwitterData;
import static com.cfp.muaavin.ui.TwitterLoginActivity.session;

public class DialogBox {

    static String[] group  =  {"A","B","C","All"};
    static String[] categories  =  new String[]{"Sexual harassment", "Incitement to violence","Trans rights"};
    public static String CategoryName;

    public static void ShowDialogBOx3(final Context context , String str , final String[] category, final int option , final String user_id ,final Activity activity, final UiUpdate uiUpdate, final boolean isTwitterData)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);

        builder.setItems(category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String dataType = "Facebook";
                String group_name = group[which]; CategoryName = category[which];
                DataLoaderHelper dataLoader = new DataLoaderHelper(context,uiUpdate,activity);
                if(isTwitterData) dataType = "Twitter";
                try {
                      dataLoader.loadReportedUsersFromDB(dataType,group_name,which,option,activity, uiUpdate);
                    } catch (Exception e) { e.printStackTrace(); }

            }
        });
        builder.show();
    }

        public static  void showErrorDialog(Context context, String heading, String Text )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(heading);

            builder.setMessage(Text);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //
                }
            });
            builder.show();

        }

        public static  void showQuestionDialog(final Context context,final Activity activity, final String user_id, final  String post_id, final String link, final boolean IsFacebookPost)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" Link");

            builder.setMessage("Here is a link , do you want to report it ?" + "\n" + link);
            builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    if(IsFacebookPost) {
                        boolean isClipboardData = true;
                        clearFacebookData();
                        DataLoaderHelper controller = new DataLoaderHelper(context,null,activity);
                        controller.loadFacebookPosts("Clipboard Posts",post_id,true);
                    }

                    else
                    {  if(session == null)
                        {
                            TwitterLoginActivity twitterFragment = new TwitterLoginActivity();
                            Bundle args = new Bundle(); args.putString("option", "Load Specific Tweet"); twitterFragment.setArguments(args);
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, twitterFragment,"TwitterFragment").addToBackStack(null).commit();
                        }   else {DataLoaderHelper controller = new DataLoaderHelper(context,null,activity); controller.loadTwitterData("Load Specific Tweet"); }
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    boolean isClipboardData = false;
                }

            });
            builder.show();
        }

        public static  void showTweetDialog(final Context context, final Activity activity, final Tweet tweet  )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" Tweet");

            builder.setMessage(tweet.text);
            builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Group name"); final String[] group = {"A","B","C"};

                    builder.setItems(categories, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        TwitterUtil.ReportTwitterDetail.infringing_user_name = tweet.user.name;
                        TwitterUtil.ReportTwitterDetail.infringing_user_id= tweet.user.idStr;
                        TwitterUtil.ReportTwitterDetail.infringing_user_profile_pic = tweet.user.profileImageUrl;
                        TwitterUtil.ReportTwitterDetail.post_Detail = tweet.text;
                        TwitterUtil.ReportTwitterDetail.post_id = tweet.idStr;
                        String group_name = group[i];
                        DataLoaderHelper dataLoader = new DataLoaderHelper(context,null,activity);
                        try {
                          dataLoader.loadReportedUsersFromDB("Twitter",group_name,i,5,null,null);
                        } catch (Exception e) {  e.printStackTrace(); }
                        }
                    }).show();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }

        public static void promptInputDialog(final  Context context, final Activity activity )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Search Group Name");
            // Set up the input
            final EditText input = new EditText(context);
            // Specify the type of input expected
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataLoaderHelper controller = new DataLoaderHelper(context,null,activity);
                    controller.loadFacebookGroups(input.getText().toString());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        public static void SelectReportOption(final Context context , String str , final String[] category, final int option , final String user_id ,final Activity activity, final UiUpdate uiUpdate, final boolean isTwitterData, final clipboard cp)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(str);

            builder.setItems(category, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for(int i = 0 ; i < activity.getFragmentManager().getBackStackEntryCount() ; i++ )
                        activity.getFragmentManager().popBackStack();

                    if (User.user_authentication == false) {  return; }

                    if(category[which].equals("Report Posts")) {
                        FriendManagement friend_management = new FriendManagement();
                        friend_management.reportFriends(context, activity);
                    }

                    if(category[which].equals("Report Clipboard Post")) {
                        cp.reportClipboardPost();
                    }

                    else if(category[which].equals("Report Tweets")) {

                        if (session == null) {
                            TwitterLoginActivity twitterFragment = new TwitterLoginActivity();
                            Bundle args = new Bundle(); args.putString("option", "LoadTweets");
                            twitterFragment.setArguments(args);
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, twitterFragment, "TwitterFragment").addToBackStack(null).commit();
                        }
                        else {
                            clearTwitterData(); DataLoaderHelper dataHandler = new DataLoaderHelper(context, uiUpdate, activity); dataHandler.loadTwitterData("LoadTweets"); }
                    }
                    else if(category[which].equals("Report Group Posts"))
                    {
                        promptInputDialog(context,activity);
                    }
                    else if(category[which].equals("Highlighted Facebook Users"))
                    {
                       FriendManagement friend_management = new FriendManagement();
                       friend_management.Highlights(context,uiUpdate,activity);
                    }

                    else if(category[which].equals("Highlighted Twitter Users"))
                    {
                        if(session == null)
                        {
                            TwitterLoginActivity twitterFragment = new TwitterLoginActivity();
                            Bundle args = new Bundle(); args.putString("option", "LoadFollowers"); twitterFragment.setArguments(args);
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, twitterFragment,"TwitterFragment").addToBackStack(null).commit();
                        } else { clearTwitterData(); DataLoaderHelper dataHandler = new DataLoaderHelper(context, uiUpdate, activity); dataHandler.loadTwitterData( "LoadFollowers"); }
                    }

                    else if(category[which].equals("Browse Reported Posts"))
                    {
                        FriendManagement friend_management = new FriendManagement(); friend_management.Browse(context,activity);
                    }
                    else if(category[which].equals("Browse Reported Tweets"))
                    {
                        FriendManagement friend_management = new FriendManagement(); friend_management.BrowseTweets(context,activity);
                    }

                    else if(category[which].equals("Manage Reports"))
                    {
                        FriendManagement friend_management = new FriendManagement(); friend_management.BrowsePost(context,user_id,activity);
                    }
                }
            });
            builder.show();
        }



}
