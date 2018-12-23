package com.qing.vasa.va.junior.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.base.BaseListFragment;
import com.qing.vasa.pubblico.entity.BaseCard;
import com.qing.vasa.va.junior.adapter.VAStudyListAdapter;
import com.qing.vasa.va.junior.view.DrawPictureActivity;

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


        //这个方法被废弃了
        /*
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Log.d("TAG41","onclick 触发了");

                switch (view.getId()){
                    case R.id.va_ll_card:
                        ToastUtils.showS("click item");
                        break;
                    case R.id.va_tv_title:
                        ToastUtils.showS("click item tv");
                        break;
                }
            }
        });
        */
        rvList.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("TAG41","onclick 触发了");

                switch (view.getId()){
                    case R.id.va_ll_card:
//                        ToastUtils.showS("click item");
                        startActivity(new Intent(getActivity(),DrawPictureActivity.class));
                        break;
                    case R.id.va_tv_title:
//                        ToastUtils.showS("click item tv");
                        startActivity(new Intent(getActivity(),DrawPictureActivity.class));
                        break;
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
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
