package com.kc.bean;

/**
 * Created by Administrator on 2017/6/4.
 */
public class UserPost {
    private String username;
    private String password;

    public UserPost(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
