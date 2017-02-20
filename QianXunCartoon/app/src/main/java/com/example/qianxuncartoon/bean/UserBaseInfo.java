package com.example.qianxuncartoon.bean;

/**
 * Created by Alex on 2017/2/19.
 * 登陆后返回的用户对象
 */
public class UserBaseInfo {

    private String flag;
    private Integer userid;
    private String username;
    private String userpwd;
    private String usermail;
    private String userimage;
    private String userregtime;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd == null ? null : userpwd.trim();
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail == null ? null : usermail.trim();
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage == null ? null : userimage.trim();
    }

    public String getUserregtime() {
        return userregtime;
    }

    public void setUserregtime(String userregtime) {
        this.userregtime = userregtime == null ? null : userregtime.trim();
    }

    @Override
    public String toString() {
        return "TbUser [userid=" + userid + ", username=" + username + ", userpwd=" + userpwd + ", usermail=" + usermail
                + ", userimage=" + userimage + ", userregtime=" + userregtime + "]";
    }


}
