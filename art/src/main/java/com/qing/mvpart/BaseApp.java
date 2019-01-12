package com.qing.mvpart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Application 基类
 * 实现业务无关的API
 * --1.管理Activity
 * 2.方法数限制
 * Created by QING on 2017/12/13.
 * -- v1.0
 * init Logger
 * -- v1.1
 * -- MultiDexApplication
 * -- v1.2
 * -- 2018-7-8 使用ActivityLifecycleCallbacks统一管理Activity
 *
 * todo 这样定义instance有问题，待优化
 */
public class BaseApp extends MultiDexApplication {

    /**
     * 应用实例
     **/
    private static BaseApp instance;
    /**
     * 记录当前栈里所有activity
     */
    private static List<Activity> activities = new ArrayList<Activity>();
    /**
     * 记录需要一次性关闭的页面
     */
    private static List<Activity> activitys = new ArrayList<Activity>();


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 统一管理Activity
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity != null) {
                    activities.add(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                // TODO: 2018/7/8 判断应用处于前台还是后台
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity != null) {
                    activities.remove(activity);
                }
            }
        });

        //init Logger
//        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 获得实例
     */
    public static BaseApp getInstance() {
        return instance;
    }


    /**
     * 获取Activity缓存列表
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 给临时Activitys添加activity
     */
    public static void addTempActivity(Activity activity) {
        if (activity != null) {
            activitys.add(activity);
        }
    }

    public static void finishTempActivity(Activity activity) {
        if (activity != null) {
            activitys.remove(activity);
            activity.finish();
        }
    }

    /**
     * 退出指定的Activitys集合（一次性的activity）
     */
    public static void exitActivitys() {
        for (Activity activity : activitys) {
            if (activity != null) {
                activitys.remove(activity);
                activity.finish();
            }
        }
        activitys.clear();
    }

    /**
     * 应用退出，结束所有的activity
     */
    public static void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activities.remove(activity);
                activity.finish();
            }
        }
        activities.clear();
        System.exit(0);
    }

    /**
     * 重启当前应用
     */
    public static void restart() {
        Intent intent = instance.getPackageManager().getLaunchIntentForPackage(instance.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        instance.startActivity(intent);
    }

    /**
     * 修复方法数超过64K的问题
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
