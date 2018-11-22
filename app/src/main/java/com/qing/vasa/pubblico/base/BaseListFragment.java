package com.qing.vasa.pubblico.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.qing.mvpart.mvp.IPresenter;
import com.qing.vasa.R;

/**
 * List Fragment Base Class
 * Created by QING on 2018/11/22.
 */

public class BaseListFragment extends BaseFragment {

    private RecyclerView rvList;

    @Override
    public int getLayoutId() {
        return R.layout.publico_fragment_base_list;
    }

    @Override
    public IPresenter createPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        rvList = (RecyclerView) rootView.findViewById(R.id.publico_rv_list);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {

    }
}
