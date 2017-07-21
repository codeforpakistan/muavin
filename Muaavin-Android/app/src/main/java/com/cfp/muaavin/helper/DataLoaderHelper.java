package com.cfp.muaavin.helper;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.facebook.FacebookUtil;
import com.cfp.muaavin.facebook.Group;
import com.cfp.muaavin.facebook.UserInterface;
import com.cfp.muaavin.loaders.GroupsLoadAsyncTask;
import com.cfp.muaavin.loaders.PostsLoadAsyncTask;
import com.cfp.muaavin.facebook.Post;
import com.cfp.muaavin.loaders.TweetsLoadAsyncTask;
import com.cfp.muaavin.twitter.TweetsAsynchronousResponse;
import com.cfp.muaavin.twitter.TwitterUtil;
import com.cfp.muaavin.ui.BrowsePost_ListView;
import com.cfp.muaavin.ui.Browse_Activity;
import com.cfp.muaavin.ui.FacebookLoginActivity;
import com.cfp.muaavin.ui.GroupsListView;
import com.cfp.muaavin.ui.Post_ListView;
import com.cfp.muaavin.ui.R;
import com.cfp.muaavin.ui.Tweet_ListView;
import com.cfp.muaavin.ui.UiUpdate;
import com.cfp.muaavin.ui.Users_ListView;
import com.cfp.muaavin.web.DialogBox;
import com.cfp.muaavin.facebook.User;
import com.cfp.muaavin.web.WebHttpGetReq;
import com.facebook.Profile;
import java.util.ArrayList;
import static com.cfp.muaavin.ui.FacebookLoginActivity.user;
import static com.cfp.muaavin.ui.TwitterLoginActivity.session;

/**
 * Created by Tooba Saeed on 11/01/2017.
 */

public class DataLoaderHelper implements TweetsAsynchronousResponse, AsyncResponsePosts, GroupsLoadAsyncTask.GroupsResponse, UserInterface {

    public Context context;
    ArrayList<Post> ClipBoard_Posts;
    String[] group = new String[]{"Sexual harassment", "Incitement to violence","Trans rights","All of the above"};
    TweetsAsynchronousResponse TwitterAsyncDelegate = this;
    UiUpdate ActivityInterface;
    Activity activity;

    public DataLoaderHelper(Context context, UiUpdate uiUpdate,Activity activity)
    {
        this.context = context; ActivityInterface = uiUpdate;
        this.activity = activity;
    }

    public boolean isTwitterUserBlocked()
    {
        if(TwitterUtil.BlockedUserIds.contains(String.valueOf(session.getUserId()))) {  return true; }
        else { return false; }
    }

    public void loadFacebookPosts(String option, String Posts,boolean ClipBoardPost)
    {
        new PostsLoadAsyncTask(option,context, this, Profile.getCurrentProfile().getId(), ClipBoardPost, Posts, new ArrayList<Post>(), new ArrayList<User>()).execute(new ArrayList<Post>());
    }

    public void loadFacebookGroups(String input)
    {
        new GroupsLoadAsyncTask(input,context,this).execute(new ArrayList<Post>());
    }

    public void loadTwitterData(String option)
    {
        if(isTwitterUserBlocked() == false)
        new TweetsLoadAsyncTask(context, TwitterAsyncDelegate,option).execute();
    }

    public void loadReportedUsersFromDB(String dataType,String Group_name,int GroupIndex, int check, Activity activity,UiUpdate userInterface) throws Exception {
        ActivityInterface = userInterface;
        if(dataType.equals("Facebook"))
        {
/*  highlight
           if(check == 0)  new WebHttpGetReq(context,null,  check,null, this).execute("http://13.76.175.64:8080/Muaavin-Web/rest/posts/Insert_Post?user_name="+AesEncryption.encrypt(Profile.getCurrentProfile().getName())+"&UserState="+AesEncryption.encrypt(User.getLoggedInUserInformation().state)+"&ReportedUserState="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.user_state)+"&Post_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_id) + "&Group_id="+ AesEncryption.encrypt(String.valueOf(FacebookUtil.ReportPostDetail.group_id))+"&Comment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.coment_id) + "&PComment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.ParentComment_ID) + "&Group_name=" + AesEncryption.encrypt(Group_name) + "&Profile_name=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) + "&user_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_id) +  "&infringing_user_name="+ AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_name)+"&Post_image=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_image)+"&userProfilePic="+AesEncryption.encrypt(user.profile_pic)+"&infringingUser_ProfilePic=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_profile_pic)+"&Comment="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.comment) +"&Post_Det=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_Detail)) ;
           else if(check == 1) new WebHttpGetReq(context,null,check,null,this).execute("http://13.76.175.64:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(Profile.getCurrentProfile().getId())+"&specificUserFriends="+true);
*/
            if(check == 0)  new WebHttpGetReq(context,null,  check,null, this).execute("http://52.176.101.55:8080/Muaavin-Web/rest/posts/Insert_Post?user_name="+AesEncryption.encrypt(Profile.getCurrentProfile().getName())+"&UserState="+AesEncryption.encrypt(User.getLoggedInUserInformation().state)+"&ReportedUserState="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.user_state)+"&Post_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_id) + "&Group_id="+ AesEncryption.encrypt(String.valueOf(FacebookUtil.ReportPostDetail.group_id))+"&Comment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.coment_id) + "&PComment_id="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.ParentComment_ID) + "&Group_name=" + AesEncryption.encrypt(Group_name) + "&Profile_name=" + AesEncryption.encrypt(Profile.getCurrentProfile().getId()) + "&user_id=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_id) +  "&infringing_user_name="+ AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_name)+"&Post_image=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_image)+"&userProfilePic="+AesEncryption.encrypt(user.profile_pic)+"&infringingUser_ProfilePic=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.infringing_user_profile_pic)+"&Comment="+AesEncryption.encrypt(FacebookUtil.ReportPostDetail.comment) +"&Post_Det=" + AesEncryption.encrypt(FacebookUtil.ReportPostDetail.post_Detail)) ;
            else if(check == 1) new WebHttpGetReq(context,null,check,null,this).execute("http://52.176.101.55:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(Profile.getCurrentProfile().getId())+"&specificUserFriends="+true);
           else if(check == 2) { Browse_Activity frag = new Browse_Activity(); Bundle args = new Bundle(); args.putString("Group_name", Group_name);args.putBoolean("isFb", true); frag.setArguments(args); FragmentManager fragmentManager = activity.getFragmentManager();  fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit(); }
            else if(check == 333) { Browse_Activity frag = new Browse_Activity(); Bundle args = new Bundle(); args.putString("Group_name", Group_name);args.putBoolean("isFb", false); frag.setArguments(args); FragmentManager fragmentManager = activity.getFragmentManager();  fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit(); }
            else if(check == 4){ BrowsePost_ListView frag = new BrowsePost_ListView(); Bundle args = new Bundle(); args.putString("Group_name", Group_name);  args.putString("user_id", Profile.getCurrentProfile().getId()); frag.setArguments(args); FragmentManager fragmentManager = activity.getFragmentManager();  fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit(); }
        }
        else if(dataType.equals("Twitter"))
        {
            String pic = UrlHelper.getEncodedUrl(User.getTwitterUserLoggedInInformation().profile_pic);
        /*    if(check == 5)  new WebHttpGetReq(context,activity,  check,null, this).execute("http://13.76.175.64:8080/Muaavin-Web/rest/TweetQuery/AddTweet?User_ID="+AesEncryption.encrypt(User.getTwitterUserLoggedInInformation().id)+"&User_Name="+AesEncryption.encrypt(User.getTwitterUserLoggedInInformation().name)+"&User_ImageUrl="+AesEncryption.encrypt(UrlHelper.getEncodedUrl(User.getTwitterUserLoggedInInformation().profile_pic))+"&Tweet_ID="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_id)+"&Message="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_Detail)+"&ImageUrl="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_image)+"&Group_Name="+AesEncryption.encrypt(Group_name)+"&InfringingUserID="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_id)+"&InfringingUserName="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_name)+"&InfringingUserProfilePic="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_profile_pic)) ;
            else if(check == 7) new WebHttpGetReq(context,activity,check,null,this).execute("http://13.76.175.64:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(User.getTwitterUserLoggedInInformation().id)+"&specificUserFriends="+true+"&isTwitterData="+true);
        */    if(check == 5)  new WebHttpGetReq(context,activity,  check,null, this).execute("http://52.176.101.55:8080/Muaavin-Web/rest/TweetQuery/AddTweet?User_ID="+AesEncryption.encrypt(User.getTwitterUserLoggedInInformation().id)+"&User_Name="+AesEncryption.encrypt(User.getTwitterUserLoggedInInformation().name)+"&User_ImageUrl="+AesEncryption.encrypt(UrlHelper.getEncodedUrl(User.getTwitterUserLoggedInInformation().profile_pic))+"&Tweet_ID="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_id)+"&Message="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_Detail)+"&ImageUrl="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.post_image)+"&Group_Name="+AesEncryption.encrypt(Group_name)+"&InfringingUserID="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_id)+"&InfringingUserName="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_name)+"&InfringingUserProfilePic="+AesEncryption.encrypt(TwitterUtil.ReportTwitterDetail.infringing_user_profile_pic)) ;
            else if(check == 7) new WebHttpGetReq(context,activity,check,null,this).execute("http://52.176.101.55:8080/Muaavin-Web/rest/Users/Highlights?name=" + AesEncryption.encrypt(Group_name)+"&user_id="+AesEncryption.encrypt(User.getTwitterUserLoggedInInformation().id)+"&specificUserFriends="+true+"&isTwitterData="+true);
        }
    }

    @Override
    public void tweetsAsynchronousResponse(ArrayList<Post> tweet, String option) {
        if(option.equals("LoadTweets"))
        {
            Tweet_ListView tweetsFrament = new Tweet_ListView();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, tweetsFrament).addToBackStack(null).commit();
        }

        else if(option.equals("LoadFollowers"))
        {
            DialogBox.ShowDialogBOx3(context, "Select Group ", group, 7, String.valueOf(session.getUserId()),null,ActivityInterface,true);
        }

        else if(option.equals("Load Specific Tweet"))
        {
            DialogBox.showTweetDialog(context, activity,TwitterUtil.Tweet);
        }
    }

    @Override
    public void getUserAndPostData(ArrayList<Post> results, String option) {

        if(option.equals("GroupPosts"))
        {
            if(FacebookUtil.Posts.size() > 0)
            {
                Post_ListView frag = new Post_ListView(); Bundle args = new Bundle();
                args.putBoolean("isTwitterData", false); args.putBoolean("ClipBoardOption", false); args.putBoolean("GroupPostOption", true); args.putString("User_id",Profile.getCurrentProfile().getId()); args.putSerializable("user_posts",FacebookUtil.Posts);
                frag.setArguments(args);
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
            }
            else{ DialogBox.showErrorDialog(context,"Error","Permission Error"); }
        }

        else if(option.equals("Clipboard Posts"))
        {   if(PostsLoadAsyncTask.getPostResponse() == true)
            {
                ClipBoard_Posts = results; // Get Asynchronous Posts Result
                FacebookUtil.users = PostsLoadAsyncTask.users; // Get users from Clipboard post
                Post_ListView frag = new Post_ListView();  Bundle args = new Bundle();
                args.putString("User_id", Profile.getCurrentProfile().getId()); args.putSerializable("user_posts", ClipBoard_Posts); args.putBoolean("GroupPostOption", false); args.putBoolean("ClipBoardOption", true); args.putBoolean("isTwitterData", false);
                frag.setArguments(args); FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
            }
        else{ DialogBox.showErrorDialog(context,"Error","Permission Error"); }
        }

        else if(option.equals("Timeline Posts"))
        {
            Users_ListView frag = new Users_ListView(); Bundle args = new Bundle();
            args.putBoolean("isTwitterData", false);  frag.setArguments(args);
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
        }

        else if(option.equals("Facebook Users"))
        {   FacebookLoginActivity.friend_list = FacebookUtil.users;
            int check = 1;
            DialogBox.ShowDialogBOx3(context, "Select Group", group, check, "",null,ActivityInterface, false);
        }
    }

    @Override
    public void getGroups(ArrayList<Group> Groups)
    {
        GroupsListView frag = new GroupsListView();  Bundle args = new Bundle();
        args.putSerializable("Groups", Groups);  frag.setArguments(args);
        FragmentManager fragmentManager =  activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).addToBackStack(null).commit();
    }

    @Override
    public void getReportedFriends(ArrayList<String> InfringingUserIds,String dataType) {

        ArrayList<User> infringing_friends = new ArrayList<User>();
        ArrayList<User> friends = FacebookLoginActivity.friend_list;
        if(dataType.equals("Twitter")) { friends = TwitterUtil.Followers ;}

        for (int j = 0; j < friends.size(); j++)
        {
            String friend_id = friends.get(j).id;
            if (InfringingUserIds.contains(friend_id)) {  infringing_friends.add(friends.get(j)); }
        }
        ActivityInterface.updateUi(infringing_friends,InfringingUserIds,dataType);
    }

    @Override
    public void getBlockedUsers(ArrayList<String> FacebookUserIds, ArrayList<String> TwitterUserIds) {

    }
}
