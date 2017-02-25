package com.example.qianxuncartoon.model;

public class TbPicture {

    private Integer episodeid;

    private String pictureurl;

    private Integer picturenum;

    public Integer getEpisodeid() {
        return episodeid;
    }

    public void setEpisodeid(Integer episodeid) {
        this.episodeid = episodeid;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl == null ? null : pictureurl.trim();
    }

    public Integer getPicturenum() {
        return picturenum;
    }

    public void setPicturenum(Integer picturenum) {
        this.picturenum = picturenum;
    }
}