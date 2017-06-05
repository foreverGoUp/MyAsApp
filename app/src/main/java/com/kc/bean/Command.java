package com.kc.bean;

import java.io.Serializable;

/**
 * @ModifiedBy 修改人
 * @Project Command
 * @Desciption 控制
 * @Author SuFH
 * @Data 2016-8-5上午11:27:57
 */
public class Command implements Serializable {
    // 公用
    private String user; // 用户账号

    private String devId; // 设备ID
    private String cmdStr; // 命令类型
    private String order; // 控制指令
    private String type; // 操作类型
    private String time; // 定时时间 MM:SS
    private Integer setT; // 温度

    private Integer roomId; // 房间编号
    private String devName; // 设备名
    public Integer addChange;   //  1 代表 新增 2 代表修改
    // 地暖
    private Integer standby;// 备用变量
    private int count;


    public Command() {
    }

    public Command(String devId, String cmdStr, String order, String type,
                   Integer setT, String devName, Integer standby) {
        super();
        this.devId = devId;
        this.cmdStr = cmdStr;
        this.order = order;
        this.type = type;
        this.setT = setT;
        this.devName = devName;
        this.standby = standby;
    }

    public Command(String devId, String cmdStr, String order) {
        this.devId = devId;
        this.cmdStr = cmdStr;
        this.order = order;
    }

    public Integer getStandby() {
        return standby;
    }

    public void setStandby(Integer standby) {
        this.standby = standby;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getCmdStr() {
        return cmdStr;
    }

    public void setCmdStr(String cmdStr) {
        this.cmdStr = cmdStr;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getAddChange() {
        return addChange;
    }

    public void setAddChange(Integer addChange) {
        this.addChange = addChange;
    }

    public Integer getSetT() {
        return setT;
    }

    public void setSetT(Integer setT) {
        this.setT = setT;
    }

}
