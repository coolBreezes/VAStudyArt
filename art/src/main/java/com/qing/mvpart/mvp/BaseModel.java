package com.qing.mvpart.mvp;

/**
 * Model层 基类
 * Created by QING on 2017/12/13.
 */
public abstract class BaseModel implements IModel {

    public BaseModel() {
        onStart();
    }

    /**
     * 执行初始化操作
     */
    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

//    @Override
//    public Context getAppContext() {
//        return BaseApp.getInstance();
//    }
}
