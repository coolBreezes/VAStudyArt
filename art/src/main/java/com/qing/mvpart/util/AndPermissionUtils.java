package com.qing.mvpart.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qing.mvpart.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.Setting;

import java.io.File;
import java.util.List;

/**
 * 权限请求工具类
 * <p>
 * v1.0 基于AndPermission封装
 * Created by QING on 2018/6/27.
 * -- 2018-8-22 21:05:17  新增适配7.0应用间文件分享
 */

public class AndPermissionUtils {

    private static final String TAG = "AndPermission";
    private static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();

    private AndPermissionUtils() {
        throw new IllegalStateException("you can't instantiate AndPermissionUtils!");
    }

    public interface RequestPermission {
        /**
         * 权限请求成功
         */
        void onRequestPermissionSuccess();

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        void onRequestPermissionFailure(List<String> permissions);

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        void onRequestPermissionFailureWithNeverAskAgain(List<String> permissions);
    }


    /**
     * 请求摄像头权限
     * --v2.0 适配8.0运行时权限策略调整，申请权限组
     */
    public static void launchCamera(Context context, RequestPermission requestPermission) {
        requestPermission(context, requestPermission,
                Permission.Group.STORAGE,
                Permission.Group.CAMERA);
    }


    /**
     * 请求外部存储的权限
     * --v2.0 适配8.0运行时权限策略调整，申请权限组
     */
    public static void externalStorage(Context context, RequestPermission requestPermission) {
        requestPermission(context, requestPermission,
                Permission.Group.STORAGE);
    }


    /**
     * 请求获取录音（麦克风对讲）权限
     * --v2.0 适配8.0运行时权限策略调整，申请权限组
     */
    public static void recordAudio(Context context, RequestPermission requestPermission) {
        requestPermission(context, requestPermission,
                Permission.Group.MICROPHONE);
    }

    /**
     * 请求获取手机状态参数
     * -- 作为生成个推唯一标识的必要参数
     */
    public static void readPhoneState(Context context, RequestPermission requestPermission) {
        requestPermission(context, requestPermission,
                Permission.READ_PHONE_STATE);
    }


    @SuppressWarnings("WeakerAccess")
    public static void requestPermission(final Context context, final RequestPermission requestPermission, final String... permissions) {
        if (context == null
                || permissions == null || permissions.length == 0) return;

        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        requestPermission.onRequestPermissionSuccess();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            requestPermission.onRequestPermissionFailureWithNeverAskAgain(permissions);
                        } else {
                            requestPermission.onRequestPermissionFailure(permissions);
                        }
                    }
                })
                .start();
    }


    @SuppressWarnings("WeakerAccess")
    public static void requestPermission(final Context context, final RequestPermission requestPermission, final String[]... permissions) {
        if (context == null
                || permissions == null || permissions.length == 0) return;

        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        requestPermission.onRequestPermissionSuccess();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            requestPermission.onRequestPermissionFailureWithNeverAskAgain(permissions);
                        } else {
                            requestPermission.onRequestPermissionFailure(permissions);
                        }
                    }
                })
                .start();
    }


    public static final class RuntimeRationale implements Rationale<List<String>> {

        @Override
        public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
            List<String> permissionNames = Permission.transformText(context, permissions);
            String message = context.getString(R.string.permission_message_permission_rationale,
                    TextUtils.join("\n", permissionNames));

            new MaterialDialog.Builder(context)
                    .cancelable(false)
                    .title(R.string.permission_title_permission_rationale)
                    .content(message)
                    .positiveText(R.string.permission_resume)
                    .positiveColor(Color.parseColor("#3FA862"))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            executor.execute();
                        }
                    })
                    .negativeText(R.string.permission_cancel)
                    .negativeColor(Color.parseColor("#4a4a4a"))
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            executor.cancel();
                        }
                    })
                    .show();
        }
    }


    /**
     * 简单权限请求实现类
     */
    public abstract static class SimpleRequestPermission implements RequestPermission {

        private Context mContext;

        public SimpleRequestPermission(Context context) {
            mContext = context;
        }


        @Override
        public void onRequestPermissionFailure(List<String> permissions) {
            ToastUtils.showS(mContext.getString(R.string.permission_request_failure));
            // 弹窗引导用户去授权
            showSettingDialog(mContext, permissions, null);
        }

        @Override
        public void onRequestPermissionFailureWithNeverAskAgain(List<String> permissions) {
            ToastUtils.showS(mContext.getString(R.string.permission_intent_setting));
            // 弹窗引导用户去授权
            showSettingDialog(mContext, permissions, null);
        }
    }

    /**
     * 显示设置权限对话框
     */
    public static void showSettingDialog(final Context context, final List<String> permissions,
                                         final Setting.Action callback) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.permission_message_permission_failed,
                TextUtils.join("\n", permissionNames));

        new MaterialDialog.Builder(context)
                .cancelable(false)
                .title(R.string.permission_title_permission_failed)
                .content(message)
                .positiveText(R.string.permission_setting)
                .positiveColor(Color.parseColor("#3FA862"))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        setPermission(context, callback);
                    }
                })
                .negativeText(R.string.permission_cancel)
                .negativeColor(Color.parseColor("#4a4a4a"))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private static void setPermission(final Context context, final Setting.Action callback) {

        // 兼容 MIUI9.6 若不开启 开发者权限-->MIUI优化，则手动修改权限管理的状态无效
        AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback(callback)
                .start();
    }


    /**
     * 兼容 MIUI9.6 若不开启 开发者权限-->MIUI优化，则手动修改权限管理的状态无效
     * 处理策略：识别为小米手机，跳转到系统自带的权限管理页面去设置
     */


    /**
     * 如果用户不再显示授权弹窗，引导用户去授权
     * 打开系统的权限管理页面
     * todo 未处理返回时onActivityResult的逻辑
     */
    private static void openSystemSettingPermission(Context context) {
        if (context == null) return;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package",
                context.getPackageName(), null));
//        activity.startActivityForResult(intent, REQUEST_CODE_SETTING);
        context.startActivity(intent);
    }


    /**
     * 适配7.0应用间文件分享
     * -- 不使用库里的方法，自定义实现方式
     */

    public static Uri getFileUrl(Context context, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(path);
            if (!file.exists()) {
                LogUtils.e("file not found");
                return null;
            }
            String authority = context.getPackageName() + ".fileprovider";
            return FileProvider.getUriForFile(context, authority, file);
        }
        return Uri.parse("file://" + path);
    }


    /**
     * 设置intent的url
     *
     * @param context
     * @param path
     * @param intent
     */
    public static void setFileUrl(Context context, String path, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }
        Uri contentUri = getFileUrl(context, path);
        intent.putExtra(android.content.Intent.EXTRA_STREAM, contentUri);
    }

}
