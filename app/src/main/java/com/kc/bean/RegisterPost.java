package com.kc.bean;

/**
 * Created by Administrator on 2017/6/4.
 * 上传json数据示例的实体
 */
public class RegisterPost {
    private String username;
    private int age;

    public RegisterPost(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
