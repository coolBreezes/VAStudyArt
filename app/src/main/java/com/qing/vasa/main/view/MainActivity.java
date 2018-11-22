package com.qing.vasa.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseActivity;
import com.qing.vasa.va.enter.view.fragment.VideoAudioStudyFragment;

/**
 * Main Page
 * Created by QING on 2018/11/21.
 */

public class MainActivity extends BaseActivity {

    private static final String TAG_FRAGMENT_VASTUDY = "VideoAudioStudyFragment";


    private FrameLayout flContent;

    private VideoAudioStudyFragment mVideoAudioStudyFragment;

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

        flContent = (FrameLayout)findViewById(R.id.main_fl_content);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {

        switchFragment(TAG_FRAGMENT_VASTUDY);
    }

    private void switchFragment(String tag) {

        Fragment targetFragment = null;
        switch (tag){
            case TAG_FRAGMENT_VASTUDY:
                targetFragment = getVideoAudioStudyFragment();
                break;
        }

        showFragment(R.id.main_fl_content,targetFragment);

    }


    private VideoAudioStudyFragment getVideoAudioStudyFragment() {
        return mVideoAudioStudyFragment = mVideoAudioStudyFragment == null ?
                new VideoAudioStudyFragment() : mVideoAudioStudyFragment;
    }
}
