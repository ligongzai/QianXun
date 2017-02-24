package com.example.qianxuncartoon.bean;

/**
 * Created by Alex on 2017/2/19.
 * 登陆后返回的用户对象
 */
public class UserBaseInfo {

    private Integer userid;
    private String username;
    private String userpwd;
    private String usermail;
    private String userimage;
    private String userregtime;
    private String usertoken;
    private Integer usertype;

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

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken == null ? null : usertoken.trim();
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }


    @Override
    public String toString() {
        return "UserBaseInfo{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", userpwd='" + userpwd + '\'' +
                ", usermail='" + usermail + '\'' +
                ", userimage='" + userimage + '\'' +
                ", userregtime='" + userregtime + '\'' +
                ", usertoken='" + usertoken + '\'' +
                ", usertype=" + usertype +
                '}';
    }
}
