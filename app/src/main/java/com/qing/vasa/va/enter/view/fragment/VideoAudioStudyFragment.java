package com.qing.vasa.va.enter.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseFragment;
import com.qing.vasa.pubblico.base.BaseListFragment;
import com.qing.vasa.pubblico.base.BaseViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Video&Audio Study Page
 * Created by QING on 2018/11/22.
 */

public class VideoAudioStudyFragment extends BaseFragment {


    private TabLayout tlTagBar;
    private ViewPager vpPager;

    private BaseViewPagerAdapter mPagerAdapter;

    private List<String> mTagData = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.va_fragment_video_audio_study;
    }

    @Override
    public IPresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        tlTagBar = (TabLayout)rootView.findViewById(R.id.va_tl_tag_bar);
        vpPager = (ViewPager)rootView.findViewById(R.id.va_vp_pager);

        // TODO: 2018/11/22 need package
        initData();
    }

    private void initData() {

        mTagData.add("Junior");
        mTagData.add("Intermediate");
        mTagData.add("Senior");

        mFragmentList.add(new BaseListFragment());
        mFragmentList.add(new BaseListFragment());
        mFragmentList.add(new BaseListFragment());

    }

    @Override
    public void setListener() {

        // TODO: 2018/11/22 rename  Base？
        mPagerAdapter = new BaseViewPagerAdapter(getActivity().getSupportFragmentManager(),
                mTagData,mFragmentList);
        vpPager.setAdapter(mPagerAdapter);
        tlTagBar.setupWithViewPager(vpPager);
        tlTagBar.setTabsFromPagerAdapter(mPagerAdapter);
    }

    @Override
    public void processLogic() {

    }
}
