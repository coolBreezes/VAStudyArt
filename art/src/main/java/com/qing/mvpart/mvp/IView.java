package com.qing.mvpart.mvp;

import android.app.Activity;

/**
 * View层 接口
 * Created by QING on 2017/12/13.
 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String msg);

    /**
     * 显示信息
     */
    void showMessage(int resId);

    /**
     * 显示错误
     */
    void showError(int code, String msg);

    /**
     * 获取Activity的上下文
     */
    Activity getActivity();

}