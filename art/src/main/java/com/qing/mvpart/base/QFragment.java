package com.qing.mvpart.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.mvpart.util.LogUtils;
import com.qing.mvpart.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Fragment 基类
 * 封装业务无关的API
 * Created by QING on 2017/12/13.
 */
public abstract class QFragment<P extends IPresenter> extends Fragment implements IFragment<P> {

    protected final String TAG = this.getClass().getSimpleName();

    protected View rootView;
    protected Context mContext;
    protected LayoutInflater mInflater;
    private P mPresenter;
//    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        if (rootView == null && getLayoutId() > 0) {
            //todo container
            rootView = inflater.inflate(getLayoutId(), null);
            //[update-test] todo butterknife 空指针
//            mUnbinder = ButterKnife.bind(this, rootView);

        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootView已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {
        if (useEventBus()) {
            EventBus.getDefault().register(this);//注册 Eventbus
        }
        mPresenter = createPresenter();
        initView(savedInstanceState); //初始化title
        setListener();
        processLogic();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);//解除注册 Eventbus
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
            //todo temp 不回收P层，回放逻辑未处理，临时处理
            //mPresenter = null;
        }
//        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
//            mUnbinder.unbind();
//            mUnbinder = null;
//        }
        mContext = null;
    }

    public P getP() {
        return mPresenter;
    }

    public LayoutInflater getInflater() {
        return mInflater = mInflater == null ?
                (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                : mInflater;
    }


    /**
     * UI逻辑相关
     */

    /**
     * 显示Fragment
     */
    protected void showFragment(int resId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(resId, fragment, fragment.getClass().getSimpleName());
            }
            // perform this action after onSaveInstanceState not error
            ft.commitAllowingStateLoss();
        }
    }


    /**
     * 页面跳转
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle bundle) {
        startActivity(clazz, bundle, false);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle bundle, boolean isFinish) {
        if (getActivity() == null) return;
        Intent intent = new Intent();
        intent.setClass(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtra(QActivity.BASE_INTENT_BUNDLE, bundle);
        }
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }


    /***************************************************
     *    工具相关方法封装 BEGIN
     ****************************************************/

    public boolean isEmpty(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return TextUtils.isEmpty(getText(obj));
    }

    public boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 获取 editTest，textView,checkbox和button的文字
     */
    public String getText(Object obj) {
        if (obj instanceof TextView) {
            return ((TextView) obj).getText().toString();
        }
        LogUtils.e("类型转换异常");
        return "";
    }

    public void toast(String msg) {
        ToastUtils.showS(msg);
    }

    public void toast(int strId) {
        ToastUtils.showS(strId);
    }

    /***************************************************
     *    工具相关方法封装 END
     ****************************************************/

}
