package com.qing.vasa.pubblico.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.qing.mvpart.base.QFragment;
import com.qing.mvpart.mvp.IPresenter;
import com.qing.mvpart.mvp.IView;
import com.qing.mvpart.util.ToastUtils;
import com.qing.vasa.R;

/**
 * Fragment 基类
 * 封装业务相关的API
 * Created by QING on 2017/12/13.
 * -- 2018-7-19  增加沉浸式
 */
public abstract class BaseFragment<P extends IPresenter> extends QFragment<P> implements IView {

    private ProgressDialog mProgressDialog;
    protected ImmersionBar mImmersionBar;

    @Override
    public void showLoading() {
        initProgressDialog();
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
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

    private void initProgressDialog() {
        if (mProgressDialog == null && mContext != null) {
            mProgressDialog = new ProgressDialog(mContext, R.style.public_dialog_load);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(true);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }


    /**
     * 沉浸式处理相关 BEGIN
     */


    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }


    /**
     * 沉浸式处理相关 END
     */


}
