package com.qing.mvpart.util;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 获取Assets目录文件的辅助类
 * Created by QING on 2018/12/15.
 */

public class AssetsUtils {

    /**
     * 获取Assets目录下的图片资源
     *
     * @param activity
     * @param fileName 包含路径
     * @return 返回Bitmap
     */
    public static Bitmap getBitmap(Activity activity, String fileName) {
        Bitmap bitmap = null;
        AssetManager assetManager = activity.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);//filename是assets目录下的图片名
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 获取Assets目录下的文本文件
     *
     * @param activity
     * @param fileName 包含相对路径
     * @return todo 要对阿拉伯语编码格式特殊处理
     */
    public static String getTextFile(Activity activity, String fileName) {
        String result = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    public static String getTextFile(Activity activity, String fileName) {
        InputStreamReader isr = null;
        BufferedReader br = null;

        StringBuilder builder = new StringBuilder();

        try {
            isr = new InputStreamReader(activity.getAssets().open(fileName));
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (isr != null) {
                    isr.close();
                    isr = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    */

    /*
    加载网页
    webView.loadUrl("file:///android_asset/html/hello.htmll");

     */

    // 加载音频
//    AssetManager assetManager = this.getAssets();
//    AssetFileDescriptor afd = assetManager.openFd(fileName);
//    mPlayer.reset();
//    mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
//    Player.prepare();
//    mPlayer.start();


}
