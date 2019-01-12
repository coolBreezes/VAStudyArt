package com.qing.mvpart.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.mvpart.util.KeyboardUtils;
import com.qing.mvpart.util.LogUtils;
import com.qing.mvpart.util.ScreenUtils;
import com.qing.mvpart.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类
 * 封装业务无关的API
 * Created by QING on 2017/12/13.
 * <p>
 * -- v1
 * --1.增加点击屏幕空白区域隐藏软键盘 API
 */
public abstract class QActivity<P extends IPresenter> extends AppCompatActivity implements IActivity<P> {

    public static final String BASE_INTENT_BUNDLE = "BASE_INTENT_BUNDLE";
    protected final String TAG = this.getClass().getSimpleName();

    protected Activity mContext;
    private P mPresenter;
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        // TODO: 2018/4/9 可能会调用  setContentView(View view) 这个方法
        mUnbinder = ButterKnife.bind(this);
        initData(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {
        if (useEventBus()) {
            EventBus.getDefault().register(this);//注册 Eventbus
        }
        mPresenter = createPresenter();

        listenNavigationState();

        initView(savedInstanceState); //初始化title
        setListener();
        processLogic();
    }

    /**
     * 监听NavigationBar的显隐状态
     *
     * 适配三星s8，此时虚拟按钮显示，华为p9因监听无效，VIRTUAL_NAVIGATION_BAR_STATE 始终为 0
     * 该值始终不变，但对应getMetrics获取到的分辨率会自动发生改变
     * 对应三星s8获取getMetrics值始终不变
     */
    private void listenNavigationState() {
        getWindow().getDecorView()
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // TODO: 2018/4/26 u
                        //适配三星s8，该监听对华为p9无效
                        ScreenUtils.VIRTUAL_NAVIGATION_BAR_STATE = visibility;
                    }
                });
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);//解除注册 Eventbus
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        mContext = null;
    }

    // TODO: 2018/4/27 改成public是否合适
    public P getP() {
        return mPresenter;
    }

    public Activity getContext() {
        return mContext;
    }


    /***************************************************
     *    UI相关
     ****************************************************/

    /**
     * 显示Fragment
     */
    protected void showFragment(int resId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
     * 隐藏Fragment
     */
    protected void hideFragment(Fragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * replace方式添加Fragment
     */
    protected void replaceFragment(int resId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(resId, fragment, fragment.getClass().getSimpleName());
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 移除fragment
     */
    @Deprecated
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
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
        // [bugFix] 空指针报错
        if (mContext == null) return;

        Intent intent = new Intent();
        intent.setClass(mContext, clazz);
        if (bundle != null) {
            intent.putExtra(BASE_INTENT_BUNDLE, bundle);
        }
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    protected LayoutInflater getInflater() {
        return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /*
            返回键返回事件的处理
            如果FragmentStack中只有1个fragment 关闭当前activity
            如果FragmentStack中还有>1数量fragment则可以removeFragment()将fragment出栈 此部分交给子类实现
        */
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 软键盘处理 BEGIN
     */


    /**
     * 点击屏幕空白区域隐藏软键盘
     * 页面多个EditText适配，需重写isMultiEditText和getEditTexList
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            boolean isShouldHideKeyboard;
            if (!isMultiEditText()) {
                View v = getCurrentFocus();
                isShouldHideKeyboard = isShouldHideKeyboard(v, ev);
            } else {
                isShouldHideKeyboard = isShouldHideKeyboard(getEditTexList(), ev);
            }
            if (isShouldHideKeyboard) {
                KeyboardUtils.hideSoftInput(getContext());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     * <p>
     */
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 处理页面中多个EditText的情况
     * 默认为false，子类需重写为true
     */
    protected boolean isMultiEditText() {
        return false;
    }

    /**
     * 子类传入edittext集合
     * 子类重写
     */
    protected View[] getEditTexList() {
        return null;
    }

    /**
     * 处理页面中多个EditText的情况
     * 子类调用，并重写 isMultiEditText
     */
    protected boolean isShouldHideKeyboard(View[] viewArr, MotionEvent event) {
        if (viewArr != null) {
            for (View view : viewArr) {
                if (!isShouldHideKeyboard(view, event)) {
                    return false;
                }
            }
        }
        return true;
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

    public void setText(Object obj, String str) {
        if (!isEmpty(str) && (obj != null)) {
            if (obj instanceof TextView) {
                ((TextView) obj).setText(str);
            }
        }
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
