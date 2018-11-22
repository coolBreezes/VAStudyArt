package com.qing.mvpart.common;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 避免内存泄漏的Handler
 * 使用的时候声明一个静态内部类（如MyHandler）继承NoLeakHandler<T>,T是MyHandler的外部类。
 * Created by QING on 2018/7/25.
 */

public abstract class NoLeakHandler<T> extends Handler {
    private WeakReference<T> mT;

    public NoLeakHandler(T outClass) {
        mT = new WeakReference<>(outClass);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mT != null && mT.get() != null) {
            T t = mT.get();
            handleMessage(msg, t);
        }
    }

    public abstract void handleMessage(Message msg, T t);
}

