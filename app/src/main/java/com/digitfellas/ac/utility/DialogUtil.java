package com.digitfellas.ac.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class DialogUtil {


    public static void showDialog(Context context,String title, String content){

        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .negativeText("Cancel")
                .show();

    }

    public static void showDialogWithCallBack(Context context,String title, String content,MaterialDialog.SingleButtonCallback singleButtonCallback){

        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText("Enable")
                .onPositive(singleButtonCallback)
                .negativeText("Cancel")
//                .onNegative(singleButtonCallback)
                .show();

    }
}
