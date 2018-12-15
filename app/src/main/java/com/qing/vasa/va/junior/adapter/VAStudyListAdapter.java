package com.qing.vasa.va.junior.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qing.vasa.R;
import com.qing.vasa.pubblico.entity.BaseCard;

import java.util.List;

/**
 * VAStudyListAdapter
 * Created by QING on 2018/11/23.
 */

public class VAStudyListAdapter extends BaseQuickAdapter<BaseCard, BaseViewHolder> {


    public VAStudyListAdapter(@Nullable List<BaseCard> data) {
        super(R.layout.va_recycle_item_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseCard item) {
        ((TextView) helper.getView(R.id.va_tv_title))
                .setText(item.getTitle());
        helper.addOnClickListener(R.id.va_ll_card);
    }

}
