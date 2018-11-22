package com.qing.mvpart.util;

/**
 * 点击事件过滤器
 * 防止过快点击造成多次点击事件
 * <p>
 * 适配ButterKnife的 onClick 事件
 * Created by QING on 2017/8/16.
 */
public class ClickFilter {

    private static final long MIN_CLICK_DELAY_TIME = 500L; // 防止连续点击的时间间隔
    private static long lastClickTime = 0L; // 上一次点击的时间
    private static long lastClickViewId = 0L; // 上一次点击的控件ID

    /**
     * 判断是否过滤掉当前的点击事件（处理同一个控件连续快速点击）
     * todo
     * @return true: 表示过滤不处理
     */
    public static boolean filter() {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = curClickTime;
            return false;
        }else {
            return true;
        }
    }

    /**
     * 判断是否过滤掉当前的点击事件
     * （多个控件下只处理单个控件快速点击，多个控件间来回快速切换不做处理）
     *
     * @return true: 表示过滤不处理
     */
    public static boolean filter(int viewId) {
        if (viewId == lastClickViewId) {
            return filter();
        }else{
            lastClickTime = System.currentTimeMillis();
            lastClickViewId = viewId;
            return false;
        }
    }
}
