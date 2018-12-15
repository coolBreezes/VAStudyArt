package com.qing.vasa.va.junior.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qing.mvpart.util.ToastUtils;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseListFragment;
import com.qing.vasa.pubblico.entity.BaseCard;
import com.qing.vasa.va.junior.adapter.VAStudyListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * VA Study Junior List Fragment
 * Created by QING on 2018/11/22.
 */

public class VAJuniorListFragment extends BaseListFragment {

    private VAStudyListAdapter mAdapter;

    private List<BaseCard> cardData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        BaseCard card = new BaseCard("1.通过三种方式绘制图片",
                BaseCard.TYPE_POP_SUB_MENU, "0x0000");
        BaseCard card1 = new BaseCard("1.通过三种方式绘制图片",
                BaseCard.TYPE_POP_SUB_MENU, "0x0000");
        BaseCard card2 = new BaseCard("1.通过三种方式绘制图片",
                BaseCard.TYPE_POP_SUB_MENU, "0x0000");

        cardData.add(card);
        cardData.add(card1);
        cardData.add(card2);
    }

    @Override
    public void setListener() {
        super.setListener();
        
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mAdapter = new VAStudyListAdapter(cardData);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()){
                    case R.id.va_ll_card:
                        ToastUtils.showS("click item");
                        break;
                }
            }
        });

        rvList.setAdapter(mAdapter);
    }

    @Override
    public void processLogic() {
        super.processLogic();

        System.out.print("");
//        mAdapter.notifyDataSetChanged();
    }
}
