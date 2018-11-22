package com.qing.mvpart.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qing.mvpart.R;
import com.qing.mvpart.constant.PermissionConstants;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 权限请求工具类
 * Created by QING on 2018/1/6.
 * <p>
 * v1.0
 * -- 基于RxPermissions封装，使用 MaterialDialog
 * v2.0
 * -- 适配8.0权限新规，申请权限组
 */
@Deprecated
public class PermissionUtils {

    private static final String TAG = "Permission";
//    public static final int REQUEST_CODE_SETTING = 0x01;


    private PermissionUtils() {
        throw new IllegalStateException("you can't instantiate PermissionUtil!");
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

    @SuppressWarnings("WeakerAccess")
    public static void requestPermission(final RequestPermission requestPermission, RxPermissions rxPermissions, String... permissions) {
        if (permissions == null || permissions.length == 0) return;

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.isEmpty()) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过,则开始申请
            rxPermissions
                    .requestEach(needRequest.toArray(new String[needRequest.size()]))
                    .buffer(permissions.length)
                    .subscribe(new Consumer<List<Permission>>() {
                        @Override
                        public void accept(List<Permission> permissions) throws Exception {
                            for (Permission p : permissions) {
                                if (!p.granted) {
                                    if (p.shouldShowRequestPermissionRationale) {
                                        Log.d(TAG, "Request permissions failure");
                                        requestPermission.onRequestPermissionFailure(Arrays.asList(p.name));
                                        return;
                                    } else {
                                        Log.d(TAG, "Request permissions failure with ask never again");
                                        requestPermission.onRequestPermissionFailureWithNeverAskAgain(Arrays.asList(p.name));
                                        return;
                                    }
                                }
                            }
                            Log.d(TAG, "Request permissions success");
                            requestPermission.onRequestPermissionSuccess();
                        }
                    });
        }
    }


    /**
     * 请求摄像头权限
     * --v2.0 适配8.0运行时权限策略调整，申请权限组
     */
    public static void launchCamera(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);
    }


    /**
     * 请求外部存储的权限
     * --v2.0 适配8.0运行时权限策略调整，申请权限组
     */
    public static void externalStorage(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                PermissionConstants.getPermissions(PermissionConstants.STORAGE));
    }


    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.SEND_SMS);
    }


    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }


    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);
    }


    /**
     * 请求获取录音（麦克风对讲）权限
     * --v2.0 适配8.0运行时权限策略调整，申请权限组
     */
    public static void recordAudio(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                PermissionConstants.getPermissions(PermissionConstants.MICROPHONE));
    }


    /**
     * 显示设置权限对话框
     */
    public static void showSettingDialog(final Activity activity) {

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .title(R.string.permission_title_permission_failed)
                .content(R.string.permission_message_permission_failed)
                .positiveText(R.string.permission_setting)
                .positiveColor(Color.parseColor("#3FA862"))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        openSettingPermission(activity);
                        dialog.dismiss();
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
     * 如果用户不再显示授权弹窗，引导用户去授权
     *
     * @param activity
     */
    public static void openSettingPermission(Activity activity) {

        if (activity == null) return;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package",
                activity.getApplication().getPackageName(), null));
//        activity.startActivityForResult(intent, REQUEST_CODE_SETTING);
        activity.startActivity(intent);
    }


    /**
     * 简单权限请求实现类
     */
    public static class SimpleRequestPermission implements RequestPermission {

        private Activity mContext;

        public SimpleRequestPermission(Activity context) {
            mContext = context;
        }

        @Override
        public void onRequestPermissionSuccess() {

        }

        @Override
        public void onRequestPermissionFailure(List<String> permissions) {
            ToastUtils.showS(mContext.getString(R.string.permission_request_failure));
            // 弹窗引导用户去授权
            showSettingDialog(mContext);
        }

        @Override
        public void onRequestPermissionFailureWithNeverAskAgain(List<String> permissions) {
            ToastUtils.showS(mContext.getString(R.string.permission_intent_setting));
            // 弹窗引导用户去授权
            showSettingDialog(mContext);
        }
    }

}

