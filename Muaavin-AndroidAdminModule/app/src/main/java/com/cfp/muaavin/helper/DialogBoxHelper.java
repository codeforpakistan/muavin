package com.cfp.muaavin.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import com.cfp.muaavin.BusinessLogic.Admin;
import com.cfp.muaavin.BusinessLogic.DialogCustomAdapter;

import java.util.ArrayList;

/**
 *
 */
public class DialogBoxHelper {
    public static String[] group = {"A", "B", "C","All"};
    public static AlertDialog alertDialog;

    public static void showDialogBox2(Context context, String title, String content )
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        builder.show();
    }
    public static void showDialogBox(final Admin admin,final Context context,final String[] category )
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Group");

        final ArrayList<String> categories = new ArrayList<String>();
        for(int i=0;i<category.length;i++)
        {
            categories.add(category[i]);
        }
         DialogCustomAdapter arrayAdapter = new DialogCustomAdapter(context, categories, admin);
        builderSingle.setAdapter(arrayAdapter,null);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builderSingle.create();
        alertDialog.show();

    }

    public static void showDialogBox(final Admin admin,final Context context,final String[] category, boolean isFb, String option )
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Group");

        final ArrayList<String> categories = new ArrayList<String>();
        for(int i=0;i<category.length;i++)
        {
            categories.add(category[i]);
        }
        DialogCustomAdapter arrayAdapter = new DialogCustomAdapter(context, categories, admin,isFb, option);
        builderSingle.setAdapter(arrayAdapter,null);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builderSingle.create();
        alertDialog.show();

    }

}
