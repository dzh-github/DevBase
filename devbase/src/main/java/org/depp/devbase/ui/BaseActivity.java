package org.depp.devbase.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import org.depp.devbase.BaseApp;
import org.depp.devbase.permission.AppSettingsDialog;
import org.depp.devbase.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Base Activity
 * Created by manfi on 2017/9/20.
 */

public abstract class BaseActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {

    protected final String TAG = getClass().getSimpleName();
    protected final boolean DEBUG = true;

    protected Activity activity;

    protected BaseUI baseUI;

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 处理触摸EditText外部收起键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体按键会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftKeyboard(v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        activity = this;
        ((BaseApp) getApplication()).addActivity(activity);
        baseUI = getCustomBaseUI();
        findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                initView();
                findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        ((BaseApp) getApplication()).removeActivity(activity);
        super.onDestroy();
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    /**
     * 可自行重写BaseUI
     */
    protected BaseUI getCustomBaseUI() {
        return new BaseUI(activity);
    }

    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int duration) {
        baseUI.showToast(msg, duration);
    }

    public void showLoading(@NonNull String msg) {
        showLoading(msg, false, false, null);
    }

    /**
     * 显示Loading
     *
     * @param msg Loading title
     */
    public void showLoading(@NonNull String msg, boolean cancelTouchOutside, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        baseUI.showLoading(msg, cancelTouchOutside, cancelable, cancelListener);
    }

    /**
     * 取消Loading对话框
     */
    public void dismissLoading() {
        baseUI.dismissLoading();
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v     EditText
     * @param event ~
     *
     * @return ~
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            // 如果点击是EditText的时间， 忽略掉
            return !(event.getX() > left) || !(event.getX() < right) || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 弹出键盘
     *
     * @param view 通常是EditText
     */
    public void showSoftKeyboard(View view) {
        baseUI.showSoftKeyboard(view);
    }

    /**
     * 隐藏键盘
     *
     * @param view 通常是EditText
     */
    public void hideSoftKeyboard(View view) {
        baseUI.hideSoftKeyboard(view);
    }

    /**
     * 没有网络回调
     */
    public void onNetworkUnavaliable() {
        baseUI.onNetworkUnavaliable();
    }

    protected abstract void initView();

    /**
     * 询问是否需要到系统设置自行打开不允许而且不再询问的权限
     *
     * @param perms ~
     */
    public void askPermanentlyDeniedPermission(String... perms) {
        String[] notGrantPermList = PermissionUtils.checkPermissions(activity, perms);
        final List<String> permanentlyDeniedPermList = new ArrayList<>();
        if (notGrantPermList != null) {
            permanentlyDeniedPermList.addAll(Arrays.asList(notGrantPermList));
        }
        if (permanentlyDeniedPermList.size() > 0) {
            List<String> needGrantPermissionGroupName = PermissionUtils.loadPermissionsGroupName(getApplicationContext(), permanentlyDeniedPermList);
            if (needGrantPermissionGroupName != null && !needGrantPermissionGroupName.isEmpty()) {
                PermissionUtils.onPermissionsPermanentlyDenied(
                        this,
                        PermissionUtils.toPermisionsGroupString(needGrantPermissionGroupName),
                        "需要在系统权限设置授予以下权限",
                        getString(android.R.string.ok),
                        getString(android.R.string.cancel),
                        (dialog, which) -> permanentlyDeniedPermissionDenied(permanentlyDeniedPermList),
                        AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
            }
        }
    }

    /**
     * 不允许权限，而且取消到系统自行打开
     *
     * @param permanentlyDeniedPerms 不允许的权限
     */
    protected void permanentlyDeniedPermissionDenied(List<String> permanentlyDeniedPerms) {

    }

    public BaseUI getBaseUI() {
        return baseUI;
    }
}