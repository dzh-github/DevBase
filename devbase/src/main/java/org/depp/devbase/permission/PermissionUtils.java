package org.depp.devbase.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * 权限检测辅助工具
 * Created by manfi on 2017/7/24.
 */

public class PermissionUtils {

    /**
     * 检查权限是否已经允许
     *
     * @param context ~
     * @param perms   ~
     * @return 没有被允许的权限
     */
    public static String[] checkPermissions(Context context, String... perms) {
        if (Build.VERSION.SDK_INT < M) {
            return new String[]{};
        }

        List<String> notGrantPermList = new ArrayList<>(perms.length);
        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                notGrantPermList.add(perm);
            }
        }
        return notGrantPermList.size() == 0 ? new String[]{} : notGrantPermList.toArray(new String[]{});
    }

    /**
     * 至少一个权限勾选了 "不再询问"
     *
     * @param object            ~
     * @param deniedPermissions ~
     * @return
     */
    public static boolean somePermissionsPermanentlyDenied(@NonNull Object object,
                                                           @NonNull String... deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (permissionPermanentlyDenied(object, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 至少一个权限没有勾选 "不再询问"
     *
     * @param object ~
     * @param perms  ~
     * @return ~
     */
    public static boolean somePermissionsNeedAskAgain(@NonNull Object object, @NonNull String... perms) {
        for (String perm : perms) {
            if (!permissionPermanentlyDenied(object, perm)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 权限勾选了 "不再询问"
     *
     * @param object           ~
     * @param deniedPermission ~
     * @return ~
     */
    public static boolean permissionPermanentlyDenied(@NonNull Object object,
                                                      @NonNull String deniedPermission) {
        return !shouldShowRequestPermissionRationale(object, deniedPermission);
    }

    /**
     * 权限已经被不再允许
     */
    public static void onPermissionsPermanentlyDenied(@NonNull Activity activity,
                                                      @NonNull String rationale,
                                                      @NonNull String title,
                                                      @NonNull String positiveButton,
                                                      @NonNull String negativeButton,
                                                      @Nullable DialogInterface.OnClickListener negativeListener,
                                                      int requestCode) {
        new AppSettingsDialog.Builder(activity, rationale)
                .setTitle(title)
                .setPositiveButton(positiveButton)
                .setNegativeButton(negativeButton, negativeListener)
                .setRequestCode(requestCode)
                .build()
                .show();
    }

    /**
     * 权限已经被不再允许
     */
    public static void onPermissionsPermanentlyDenied(@NonNull android.app.Fragment fragment,
                                                      @NonNull String rationale,
                                                      @NonNull String title,
                                                      @NonNull String positiveButton,
                                                      @NonNull String negativeButton,
                                                      @Nullable DialogInterface.OnClickListener negativeListener,
                                                      int requestCode) {
        new AppSettingsDialog.Builder(fragment, rationale)
                .setTitle(title)
                .setPositiveButton(positiveButton)
                .setNegativeButton(negativeButton, negativeListener)
                .setRequestCode(requestCode)
                .build()
                .show();
    }

    /**
     * 权限已经被不再允许
     */
    public static void onPermissionsPermanentlyDenied(@NonNull Fragment fragment,
                                                      @NonNull String rationale,
                                                      @NonNull String title,
                                                      @NonNull String positiveButton,
                                                      @NonNull String negativeButton,
                                                      @Nullable DialogInterface.OnClickListener negativeListener,
                                                      int requestCode) {
        new AppSettingsDialog.Builder(fragment, rationale)
                .setTitle(title)
                .setPositiveButton(positiveButton)
                .setNegativeButton(negativeButton, negativeListener)
                .setRequestCode(requestCode)
                .build()
                .show();
    }

    /**
     * 输出所需字符串
     *
     * @param perms ~
     * @return xx、xx、xx、xx
     */
    public static String toPermisionsGroupString(List<String> perms) {
        if (perms != null && !perms.isEmpty()) {
            return perms.toString().replace("[", "").replace("]", "");
        }
        return "";
    }

    /**
     * 找出权限所属组别名称
     * <p>
     * 实际上到系统-应用-权限下打开对应的权限显示的是组名。
     * </p>
     *
     * @param context ~
     * @param perms   ~
     * @return ~
     */
    public static List<String> loadPermissionsGroupName(@NonNull Context context, @NonNull List<String> perms) {

        if (perms.isEmpty()) {
            return null;
        }
        List<String> permsGroupName = new ArrayList<>(perms.size());
        try {
            for (String perm : perms) {
                PermissionInfo permissionInfo = context.getPackageManager().getPermissionInfo(perm, PackageManager.GET_META_DATA);
                PermissionGroupInfo permissionGroupInfo = context.getPackageManager().getPermissionGroupInfo(permissionInfo.group, PackageManager.GET_META_DATA);
                String permissionName = (String) permissionGroupInfo.loadLabel(context.getPackageManager());
                if (!permsGroupName.contains(permissionName)) {
                    permsGroupName.add(permissionName);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permsGroupName;
    }

    @TargetApi(M)
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }
}