package com.qing.mvpart.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.qing.mvpart.R;

/**
 * 图片加载库辅助工具类
 * Created by QING on 2018/8/16.
 */

public class ImageLoaderUtils {

    private static final String TAG = ImageLoaderUtils.class.getSimpleName();

    /**
     * 加载图片(默认)
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.publico_img_place_holder) //占位图
                .error(R.color.white)       //错误图
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options).into(imageView);
    }


    /**
     * 加载图片,提供回调接口
     * -- 去掉占位图，避免图片加载大小显示异常
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView,
                                 final Callback callback) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
//                .placeholder(R.drawable.publico_img_place_holder) //占位图
                .error(R.color.white)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(TAG, "加载图片失败");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return callback != null && callback.onResourceReady(resource);
                    }
                })
                .into(imageView);
    }


    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .circleCrop()//设置圆形
                .placeholder(R.drawable.publico_img_place_holder)
                .error(R.color.white)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options).into(imageView);
    }


    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * 加载图片缩略图
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadThumb(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.publico_img_place_holder) //占位图
                .error(R.color.white)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options).into(imageView);
    }

    /**
     * 滑动时暂停图片加载
     *
     * @param context
     */
    public static void pauseLoad(Context context) {
        if (!isPausedLoad(context))
            Glide.with(context).pauseRequests();
    }

    /**
     * 停止滑动后恢复图片加载
     *
     * @param context
     */
    public static void resumeLoad(Context context) {
        if (isPausedLoad(context))
            Glide.with(context).resumeRequests();
    }


    /**
     * 判断当前是否暂停加载图片
     *
     * @param context
     */
    private static boolean isPausedLoad(Context context) {
        return Glide.with(context).isPaused();
    }


    public interface Callback {

        /**
         * 图片加载完成回调
         *
         * @param resource
         * @return true   直接在回调中加载图片， false   表示仍在into中处理图片
         */
        boolean onResourceReady(Drawable resource);
    }

}
