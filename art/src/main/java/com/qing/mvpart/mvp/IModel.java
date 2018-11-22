package com.qing.mvpart.mvp;

/**
 * Model层 接口
 * Created by QING on 2017/12/13.
 */
public interface IModel {

    /**
     * 做一些初始化操作
     */
    void onStart();

    void onDestroy();

    /**
     * 获取App全局Context
     * todo think 是否合适
     */
//    Context getAppContext();
}
