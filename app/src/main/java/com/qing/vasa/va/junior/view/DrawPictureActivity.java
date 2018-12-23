package com.qing.vasa.va.junior.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.mvpart.util.AssetsUtils;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseActivity;

/**
 * 1.三种方式绘制图片
 * Created by QING on 2018/11/28.
 */

public class DrawPictureActivity extends BaseActivity implements View.OnClickListener {

    public static final String ASSERTS_IMAGE_LOGO = "image/logo.jpg";

    private Button btShow1;
    private ImageView ivShow1;

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
}
