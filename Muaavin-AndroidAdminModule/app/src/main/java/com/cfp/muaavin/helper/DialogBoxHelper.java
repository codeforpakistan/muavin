package com.cfp.muaavin.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cfp.muaavin.BusinessLogic.Admin;

/**
 *
 */
public class DialogBoxHelper {
    static String[] group = {"A", "B", "C","All"};
    public static void showDialogBox(final Admin admin,final Context context,final String[] category )
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Group");
        builder.setItems(category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                String GroupName = group[index];
                try {   admin.deletePost(context, GroupName); }
                catch (Exception e) { e.printStackTrace(); }
            }
        });
        builder.show();
    }
}
