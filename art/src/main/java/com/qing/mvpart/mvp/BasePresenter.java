package com.qing.mvpart.mvp;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Presenter层 基类
 * Created by QING on 2017/12/13.
 */
public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter {

    protected final String TAG = this.getClass().getSimpleName();

    protected Context mContext;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private M mModel;
    private V mView;

    public BasePresenter() {
        this(null);
    }

    /**
     * 如果当前页面不需要操作数据,只需要 View 层,则使用此构造函数
     */
    public BasePresenter(V rootView) {
        this(null, rootView);
    }

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     */
    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mView = rootView;
        onStart();
    }

    /**
     * 执行初始化操作
     */
    @Override
    public void onStart() {
        // 获取上下文
        if (isViewAttached()) {
            mContext = getV().getActivity();
        }
    }

    @Override
    public void onDestroy() {
        mDisposables.clear();
        if (mModel != null) {
            mModel.onDestroy();
            // TODO: 2018/5/16 think bug 子线程调用M 子类重写？
//            但有的时候，仍然需要在v销毁做的操作
//（退到后台，停止播放）
            mModel = null;
        }
        this.mView = null;
    }

    protected M getM() {
        return mModel;
    }

    protected V getV() {
        return mView;
    }

    /**
     * 获取Presenter绑定的上下文
     */
    protected Context getContext() {
        return mContext;
    }


    /**
     * 获取Presenter绑定的上下文
     */
    protected CompositeDisposable getDisposables() {
        return mDisposables = mDisposables == null ? new CompositeDisposable() : mDisposables;
    }

    /**
     * 绑定View，通常在初始化中调用该方法
     */
    protected void attachView(V view) {
        mView = view;
    }

    /**
     * 断开View，一般在onDestroy中调用
     */
    protected void detachView(V view) {
        mView = null;
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求时都要先调用该方法检查是否与View建立连接
     */
    protected boolean isViewAttached() {
        return mView != null;
    }


    /**
     * 取消订阅
     */
    protected void cancelDisposable(Disposable d) {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }

    /**
     * 简单的Observer处理类
     */
    protected abstract class SimpleObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(Disposable d) {
            getDisposables().add(d);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("BasePresenter",TAG+" : e = " + e.getMessage());
            if (isViewAttached()) getV().hideLoading();
        }

        @Override
        public void onComplete() {

        }
    }

}