package com.daShen.zoneView.model;

/**
 * @param roomId   作为回调传到view外部的参数值
 * @param roomName 作为viwe Y轴显示房间的名称数据
 * @ModifiedBy 修改人
 * @Project RoomInfo.java
 * @Desciption 房间信息
 * @Author SuFH
 * @Data 2017年5月26日上午9:59:56
 * <p>
 * 自定义我的地盘用到的参数
 */
public class RoomInfo {
    private Integer roomId;// 房间id
    private String roomName;// 房间名称

    public RoomInfo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoomInfo(Integer roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }
}
