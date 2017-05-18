package com.kc.bean;

/**
 * @ModifiedBy 修改人
 * @Project DeviceList.java
 * @Desciption 设备列表
 * @Author SuFH
 * @Data 2017年3月15日上午8:50:34
 */
public class DeviceList {
    private String id;// 设备ID
    private String name;// 设备名称
    private String state; // 设备状态 可能为空
    private String sn;// 注册码
    private Integer roomId;// 房间ID
    private String type;// 设备类型

    public DeviceList(String id, String name, String sn, Integer roomId,
                      String type) {
        super();
        this.id = id;
        this.name = name;
        this.sn = sn;
        this.roomId = roomId;
        this.type = type;
        this.state = "1";
    }

    public DeviceList() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
