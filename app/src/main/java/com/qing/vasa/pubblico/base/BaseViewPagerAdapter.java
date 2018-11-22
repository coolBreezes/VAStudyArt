package com.qing.vasa.pubblico.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Base Fragment ViewPager Adapter
 * Created by QING on 2018/11/22.
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> mTitleData;
    private List<Fragment> mFragmentList;

    public BaseViewPagerAdapter(FragmentManager fm, List<String> titleData, List<Fragment> fragmentList) {
        super(fm);
        mTitleData = titleData;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleData.get(position);
    }
}
