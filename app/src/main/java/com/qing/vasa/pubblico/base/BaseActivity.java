package com.qing.vasa.pubblico.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import com.qing.mvpart.base.QActivity;
import com.qing.mvpart.mvp.IPresenter;
import com.qing.mvpart.mvp.IView;
import com.qing.mvpart.util.ToastUtils;
import com.qing.vasa.R;

/**
 * Activity 基类
 * 封装业务相关的API
 * Created by QING on 2017/12/13.
 */
public abstract class BaseActivity<P extends IPresenter> extends QActivity<P> implements IView {

    private ProgressDialog mProgressDialog;
    // TODO: 2018/11/21 处理沉浸式
//    private ImmersionBar immersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        immersionBar = ImmersionBar
//                .with(this)
//                //.navigationBarColor(R.color.white)  // 不设置导航栏背景
//                .statusBarDarkFont(true, 0.2f);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            immersionBar.fitsSystemWindows(true);
//        }
//        immersionBar.init();

    }


    @Override
    public void showLoading() {
        initProgressDialog();
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            // 需要先show，否则会显示异常
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.publico_load_dialog);
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        ToastUtils.showS(msg);
    }

    @Override
    public void showMessage(int resId) {
        ToastUtils.showS(resId);
    }

    @Override
    public void showError(int code, String msg) {
        ToastUtils.showS(msg + " " + code);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private void initProgressDialog() {
        if (mProgressDialog == null && mContext != null) {
            mProgressDialog = new ProgressDialog(mContext, R.style.public_dialog_load);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (immersionBar != null) {
//            immersionBar.destroy();
//        }
    }


}
