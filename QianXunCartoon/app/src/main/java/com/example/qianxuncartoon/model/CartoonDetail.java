package com.example.qianxuncartoon.model;

/**
 * Created by 咸鱼 on 2017/2/20.
 */

public class CartoonDetail {
    private String picCover ;    //图片封面地址
    private String description;  //漫画简介
    private String TbEpisod;     //漫画集数

    public String getPicCover() {
        return picCover;
    }

    public void setPicCover(String picCover) {
        this.picCover = picCover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTbEpisod() {
        return TbEpisod;
    }

    public void setTbEpisod(String tbEpisod) {
        TbEpisod = tbEpisod;
    }
}
