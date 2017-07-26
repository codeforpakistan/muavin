package com.cfp.muaavin.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.cfp.muaavin.facebook.AsyncResponsePosts;
import com.cfp.muaavin.web.DialogBox;

/**
 *
 */
public class ClipBoardHelper {
    //https://m.facebook.com/story.php?story_fbid=10158184720635506&id=572570505
    //https://m.facebook.com/story.php?story_fbid=10154624315373451&id=325342768450
    //https://m.facebook.com/story.php?story_fbid=10154624315373451&id=325342768450
    //https://m.facebook.com/story.php?story_fbid=1740501776262746&substory_index=36&id=100009088212554
    //https://m.facebook.com/story.php?story_fbid=1608279239485001&id=100009088212554
    //https://m.facebook.com/story.php?story_fbid=10154624315373451&id=325342768450
    //https://twitter.com/Asad_Umar/status/828527999951761408
    public static ClipboardManager clipboard;
    public static void getPostFromClipBoard(Context context , String user_id, Activity activity)
    {
        boolean IsFacebookPost = false;
        clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //ClipData clip = ClipData.newPlainText("url", "https://m.facebook.com/story.php?story_fbid=10154624315373451&id=325342768450");
        //clipboard.setPrimaryClip(clip);

        String ClipboardData = "";///https://twitter.com/Asad_Umar/status/828527999951761408
        if(clipboard.hasPrimaryClip())//https://m.facebook.com/story.php?story_fbid=1341003545930283&substory_index=0&id=487101601320486
        {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);//https://m.facebook.com/story.php?story_fbid=10206185117027156&id=1841119553
            ClipboardData = String.valueOf( item.getText());

            if(ClipboardData!=null)
            {
                String post_id = "" ;
                if(ClipboardData.contains("m.facebook.com")){ IsFacebookPost = true;  post_id =  UrlHelper.getQueryFieldsFromURL( ClipboardData); }
                else { String[] StringArr = ClipboardData.split("/"); post_id = StringArr[StringArr.length-1]; }
                DialogBox.showQuestionDialog(context,activity,  user_id, post_id, ClipboardData,IsFacebookPost );
            }
        }
    }
}
