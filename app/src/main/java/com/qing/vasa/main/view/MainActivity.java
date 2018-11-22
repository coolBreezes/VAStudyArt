package com.qing.vasa.main.view;

import android.os.Bundle;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseActivity;

/**
 * Main Page
 * Created by QING on 2018/11/21.
 */

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    public IPresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {

        showLoading();
    }
}
