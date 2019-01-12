package com.qing.vasa.va.junior.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.mvpart.util.AssetsUtils;
import com.qing.mvpart.util.ToastUtils;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseActivity;

import butterknife.BindView;

/**
 * 1.三种方式绘制图片
 * Created by QING on 2018/11/28.
 */

public class DrawPictureActivity extends BaseActivity implements View.OnClickListener,
        SurfaceHolder.Callback {

    public static final String ASSERTS_IMAGE_LOGO = "image/logo.jpg";

    private Button btShow1;
    private ImageView ivShow1;

    @BindView(R.id.va_bt_show_3)
    Button btShowSv;
    @BindView(R.id.va_sv_show)
    SurfaceView svShow;

    private SurfaceHolder holder;
    private DrawThread drawThread;

    @Override
    public int getLayoutId() {
        return R.layout.va_activity_junior_draw_picture;
    }

    @Override
    public IPresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        btShow1 = (Button) findViewById(R.id.va_bt_show_1);
        ivShow1 = (ImageView) findViewById(R.id.va_iv_show_1);
    }

    @Override
    public void setListener() {

        btShow1.setOnClickListener(this);
        btShowSv.setOnClickListener(this);

        holder = svShow.getHolder();
        holder.addCallback(this);
        drawThread = new DrawThread(holder);
    }

    @Override
    public void processLogic() {


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.va_bt_show_1:
                showImageByImageView();
                break;
            case R.id.va_bt_show_3:
                ToastUtils.showS("click button2");
                break;
        }
    }

    /**
     * 1.1 使用ImageView加载图片
     * -- 图片在Assets目录下
     */
    private void showImageByImageView() {

        Bitmap bitmap = AssetsUtils.getBitmap(getActivity(),
                ASSERTS_IMAGE_LOGO);

        ivShow1.setImageBitmap(bitmap);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    class DrawThread extends Thread {

        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {

            Canvas canvas = surfaceHolder.lockCanvas(); // 锁定画布
            canvas.drawColor(Color.RED);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Rect rect = new Rect(100, 40, 300, 250);
            canvas.drawRect(rect, paint);
            canvas.drawText("hello", 100, 200, paint);

            surfaceHolder.unlockCanvasAndPost(canvas); //结束锁定画布，并提交修改
        }
    }

}
