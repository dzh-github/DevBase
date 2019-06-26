package org.depp.devbase.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


public class BaseUI {

    protected Activity activity;

    private Toast toast;
    private MaterialDialog loadingDialog;

    public BaseUI(Activity activity) {
        this.activity = activity;
    }

    /**
     * 显示Toast
     *
     * @param msg      Toast
     * @param duration {@link Toast#LENGTH_SHORT}, {@link Toast#LENGTH_LONG}
     */
    public void showToast(String msg, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(activity, msg, duration);
        toast.show();
    }

    /**
     * 显示Loading
     *
     * @param msg Loading title
     */
    public void showLoading(@NonNull String msg, boolean cancelTouchOutside, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        if (loadingDialog == null) {
            loadingDialog = new MaterialDialog.Builder(activity)
                    .progress(true, 0)
                    .build();
        }
        loadingDialog.setContent(msg);
        loadingDialog.setCanceledOnTouchOutside(cancelTouchOutside);
        loadingDialog.setCancelable(cancelable);
        if (cancelable && cancelListener != null) {
            loadingDialog.setOnCancelListener(cancelListener);
        } else {
            loadingDialog.setOnCancelListener(null);
        }
        loadingDialog.show();
    }

    /**
     * 取消Loading对话框
     */
    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 弹出键盘
     *
     * @param view 通常是EditText
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param view 通常是EditText
     */
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 没有网络回调
     */
    public void onNetworkUnavaliable() {
        showToast("没有网络", Toast.LENGTH_LONG);
    }
}
