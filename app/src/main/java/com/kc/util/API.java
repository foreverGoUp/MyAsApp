package com.kc.util;

public class API {

    //TODO 设备分类
    public static final String DEVICE_WINDOWN = "windown";
    public static final String DEVICE_AIR_BOX = "airbox";
    public static final String DEVICE_LAMP = "lamp";
    public static final String DEVICE_NATHER = "nather";
    public static final String DEVICE_VEIGA = "veiga";

    //TODO 返回code,标识数据类型bean
    /**
     * 用户请求返回
     */
    public static final int USER = 0x01;//用户请求，User
    /**
     * 用户请求返回
     */
    public static final int USER_ALL_DEVICE_INFO = 0x02;//用户请求返回所有设备信息
    public static final int USERLIST = 0x03;//用户列表请求，User 头像列表
    /**
     * 控制日志
     */
    public static final int USER_LOG = 0x04;//用户列表请求，User 头像列表

    /**
     * 单控返回
     */
    public static final int DEVICE_CONTROL = 0x10;//控制,Command

    /**
     * 捷控返回
     */
    public static final int CONVENIENT = 0x11;//捷控，Convenience
    /**
     * 定时返回
     */
    public static final int TIMING = 0x12;//定时,

    /**
     * 捷控所有信息数组
     */
    public static final int CONVENIENT_ARRAY = 0x13;//捷控返回所有信息
    /**
     * 定时所有信息数组
     */
    public static final int TIMING_ARRAY = 0x14;//定时返回所有信息
    /**
     * 返回歌曲列表
     */
    public static final int MUSIC_LIST = 0x15;

    /**
     * 无参返回
     */
    public static final int DATA_INT = 0x20;//失败
    /**
     * 设备下线
     */
    public static final int DEVICE_OFFLINE = 0x21;


    //TODO 通知标识，可能是失败通知
    /**
     * 用户已在别处登陆
     */
    public static final int USER_REQUEST_LOGIN_ELSEWHERE = 0X00;
    /**
     * 账号或密码错误
     */
    public static final int USER_REQUEST_LOGIN_ERR = 0X01;
    /**
     * 账号不存在
     */
    public static final int USER_REQUEST_ACCOUNT_INEXISTENCE = 0X02;
    /**
     * 无头像
     */
    public static final int USER_REQUEST_HEAD_IMAGE_INEXISTENCE = 0X03;
    /**
     * 刷新头像失败
     */
    public static final int USER_REQUEST_HEAD_IMAGE_ERR = 0X04;
    /**
     * 修改密码失败
     */
    public static final int USER_REQUEST_CHANGE_PASSWORD_ERR = 0X05;
    /**
     * 设置失败
     */
    public static final int USER_REQUEST_SET_ERR = 0X06;
    /**
     * 验证码错误
     */
    public static final int USER_REQUEST_SMS_ERR = 0X07;
    /**
     * 验证码失效
     */
    public static final int USER_REQUEST_SMS_LOSE_EFFICACY = 0X08;

    /**
     * 验证码生成失败
     */
    public static final int USER_REQUEST_SMS_CREATE_ERR = 0X0B;

    /**
     * 注册包格式错误
     */
    public static final int USER_REQUEST_SERIAL_NUMBER_ERR = 0X0D;


    /**
     * 授权失败
     */
    public static final int USER_REQUEST_AUTHORIZATION_ERR = 0X0A;

    /**
     * 控制失败
     */
    public static final int CONTROL_JUR_INVALID = 0x09;//无权限
    /**
     * 控制失败
     */
    public static final int NOTICE_DEVICE_CTRL_FAIL = 0x10;//控制失敗

    /**
     * 捷控操作失败
     */
    public static final int CONVENIENT_REQUEST_ERR = 0x11;//捷控操作失败


    /**
     * 定时操作失败
     */
    public static final int TIMING_REQUEST_ERR = 0x12;//定时操作失败
    /**
     * 获取设备信息失败
     */
    public static final int USER_REQUEST_GET_DEVICEINFO_ERR = 0x13;
    /**
     * 注册失败:手机号重复,验证手机号是否存在
     */
    public static final int REGISTER_FAIL_PHONE_EXIST = 0X14;
    /**
     * 获取失败
     */
    public static final int REQUEST_GET_ERR = 0x15;
    /**
     * 设备全部离线
     */
    public static final int NOTICE_DEVICE_ALL_OFFLINE = 0x16;


}
