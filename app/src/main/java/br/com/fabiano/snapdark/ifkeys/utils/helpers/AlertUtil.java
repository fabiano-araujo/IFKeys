package br.com.fabiano.snapdark.ifkeys.utils.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import br.com.fabiano.snapdark.ifkeys.BuildConfig;
import br.com.fabiano.snapdark.ifkeys.Main;


/**
 * Created by Fabiano on 03/07/2017.
 */

public class AlertUtil {
    public static ProgressDialog progressDialog;
    public static AlertDialog alertDialog;
    public static AlertDialog.Builder alertDialogBuilder;
    public static void log(String tag, String m){
        if (BuildConfig.DEBUG && m != null) {
           Log.i(tag,m);
        }
    }
    public static void dismissWithCheck(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

                // if the Context used here was an activity AND it hasn't been finished or destroyed
                // then dismiss it
                if (context instanceof Activity) {

                    // Api >=17
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                            dismissWithTryCatch(dialog);
                        }
                    } else {

                        // Api < 17. Unfortunately cannot check for isDestroyed()
                        if (!((Activity) context).isFinishing()) {
                            dismissWithTryCatch(dialog);
                        }
                    }
                } else
                    // if the Context used wasn't an Activity, then dismiss it too
                    dismissWithTryCatch(dialog);
            }
            dialog = null;
        }
    }

    public static void dismissWithTryCatch(Dialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
            progressDialog = null;
        }
    }

    //Show Alerts
    public static void showAlert(AlertDialog alert, Context activity){
        try {
            alertDialog =alert;
            if(((Main)activity).mainIsRunning.get() != null && !((Main)activity).mainIsRunning.get().isFinishing()
                    && !((Activity)activity).isFinishing() && !((Main)activity).pausado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Main)activity).isDestroyed()){
                        alert.show();
                    }
                }else{
                    alert.show();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public static void showAlert(AlertDialog.Builder alert, Context activity){
        try {
            alertDialogBuilder = alert;
            if(((Main)activity).mainIsRunning.get() != null && !((Main)activity).mainIsRunning.get().isFinishing()
                    && !((Activity)activity).isFinishing() && !((Main)activity).pausado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Main)activity).isDestroyed()){
                        alert.show();
                    }
                }else{
                    alert.show();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public static void showAlert(Toast alert, Context activity){
        try {
            if(((Main)activity).mainIsRunning.get() != null &&
                    !((Main)activity).mainIsRunning.get().isFinishing()
                    && !((Activity)activity).isFinishing() && !((Main)activity).pausado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Main)activity).isDestroyed()){
                        alert.show();
                    }
                }else{
                    alert.show();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static void showAlert(Snackbar alert, Context activity){
        try {
            if(((Main)activity).mainIsRunning.get() != null && !((Main)activity).mainIsRunning.get().isFinishing()
                    && !((Activity)activity).isFinishing() && !((Main)activity).pausado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Main)activity).isDestroyed()){
                        alert.show();
                    }
                }else{
                    alert.show();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static void showAlert(ProgressDialog progressDialog, Activity activity) {
        try {
            progressDialog = progressDialog;
            if(((Main)activity).mainIsRunning.get() != null && !((Main)activity).mainIsRunning.get().isFinishing()
                    && !((Activity)activity).isFinishing() && !((Main)activity).pausado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Main)activity).isDestroyed()){
                        progressDialog.show();
                    }
                }else{
                    progressDialog.show();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public static boolean isRunning(Context activity){
        try {
            if(((Main)activity).mainIsRunning.get() != null &&
                    !((Main)activity).mainIsRunning.get().isFinishing()
                    && !((Activity)activity).isFinishing() && !((Main)activity).pausado){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Main)activity).isDestroyed()){
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}
