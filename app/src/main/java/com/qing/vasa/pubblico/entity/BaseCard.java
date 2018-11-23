package com.qing.vasa.pubblico.entity;

/**
 * Bae Card Entity
 * todo rename
 * Created by QING on 2018/11/22.
 */

public class BaseCard {

    public static final int TYPE_JUMP_TO_DETAIL = 0;    // 跳转到详情
    public static final int TYPE_POP_SUB_MENU = 1;      // 弹出子菜单

    private String title = "";
    private String desc = "";
    private int type;
    private String tag = "";

    public BaseCard(String title, int type, String tag) {
        this.title = title;
        this.type = type;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
