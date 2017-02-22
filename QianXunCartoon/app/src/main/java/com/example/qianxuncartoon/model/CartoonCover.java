package com.example.qianxuncartoon.model;

/**
 * Created by 咸鱼 on 2017/2/16.
 */

public class CartoonCover {

    private String comiccover;//图片地址
    private String comicname;//漫画名称
    private int comicid;      //漫画唯一标识符

    public String getComicname() {
        return comicname;
    }

    public void setComicname(String comicname) {
        this.comicname = comicname;
    }

    public int getComicid() {
        return comicid;
    }

    public void setComicid(int comicid) {
        this.comicid = comicid;
    }

    public String getComiccover() {
        return comiccover;
    }

    public void setComiccover(String comiccover) {
        this.comiccover = comiccover;
    }
}
