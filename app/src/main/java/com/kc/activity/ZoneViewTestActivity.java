package com.kc.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.daShen.zoneView.ZoneView;
import com.daShen.zoneView.model.DeviceList;
import com.daShen.zoneView.model.DeviceType;
import com.daShen.zoneView.model.RoomInfo;
import com.kc.myasapp.R;

import java.util.Arrays;
import java.util.List;

public class ZoneViewTestActivity extends AppCompatActivity {

    private ZoneView zoneView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_view_test);
        zoneView = (ZoneView) findViewById(R.id.zoneView);


        List<RoomInfo> roomInfos = Arrays.asList(new RoomInfo[]{
                new RoomInfo(1, "客厅"),
                new RoomInfo(2, "卫生间"),
                new RoomInfo(3, "儿童房"),
                new RoomInfo(4, "餐厅"),
                new RoomInfo(5, "餐厅5"),
                new RoomInfo(6, "餐厅6")
        });
        List<DeviceType> deviceTypes = Arrays.asList(new DeviceType[]{
                new DeviceType("d1", "电视"),
                new DeviceType("d2", "音乐"),
                new DeviceType("d3", "窗户"),
                new DeviceType("d4", "新风"),
                new DeviceType("d5", "净化"),
                new DeviceType("d6", "灯光"),
                new DeviceType("d7", "空调"),
                new DeviceType("d8", "洗衣机"),
                new DeviceType("d9", "扫地机")
        });

        List<DeviceList> deviceLists = Arrays.asList(new DeviceList[]{
                new DeviceList("", "", null, 1, "扫地机"),
                new DeviceList("", "", null, 2, "扫地机"),
                new DeviceList("", "", "1", 3, "扫地机"),
                new DeviceList("", "", "1", 4, "扫地机"),
                new DeviceList("", "", "0", 5, "扫地机"),
                new DeviceList("", "", "0", 6, "扫地机"),
                new DeviceList("", "", null, 1, "洗衣机"),
                new DeviceList("", "", null, 2, "洗衣机"),
                new DeviceList("", "", "1", 3, "洗衣机"),
                new DeviceList("", "", "1", 4, "洗衣机"),
                new DeviceList("", "", "0", 5, "洗衣机"),
                new DeviceList("", "", "0", 6, "洗衣机"),
                new DeviceList("", "", null, 1, "空调"),
                new DeviceList("", "", null, 2, "空调"),
                new DeviceList("", "", "1", 3, "空调"),
                new DeviceList("", "", "1", 4, "空调"),
                new DeviceList("", "", "0", 5, "空调"),
                new DeviceList("", "", "0", 6, "空调"),
                new DeviceList("", "", null, 1, "灯光"),
                new DeviceList("", "", null, 2, "灯光"),
                new DeviceList("", "", "1", 3, "灯光"),
                new DeviceList("", "", "1", 4, "灯光"),
                new DeviceList("", "", "0", 5, "灯光"),
                new DeviceList("", "", "0", 6, "灯光"),
                new DeviceList("", "", null, 1, "净化"),
                new DeviceList("", "", null, 2, "净化"),
                new DeviceList("", "", "1", 3, "净化"),
                new DeviceList("", "", "1", 4, "净化"),
                new DeviceList("", "", "0", 5, "净化"),
                new DeviceList("", "", "0", 6, "净化"),
                new DeviceList("", "", null, 1, "新风"),
                new DeviceList("", "", null, 2, "新风"),
                new DeviceList("", "", "1", 3, "新风"),
                new DeviceList("", "", "1", 4, "新风"),
                new DeviceList("", "", "0", 5, "新风"),
                new DeviceList("", "", "0", 6, "新风"),
                new DeviceList("", "", null, 1, "电视"),
                new DeviceList("", "", null, 2, "电视"),
                new DeviceList("", "", "1", 3, "电视"),
                new DeviceList("", "", "1", 4, "电视"),
                new DeviceList("", "", "0", 5, "电视"),
                new DeviceList("", "", "0", 6, "电视"),
                new DeviceList("", "", null, 1, "音乐"),
                new DeviceList("", "", null, 2, "音乐"),
                new DeviceList("", "", "1", 3, "音乐"),
                new DeviceList("", "", "1", 4, "音乐"),
                new DeviceList("", "", "0", 5, "音乐"),
                new DeviceList("", "", "0", 6, "音乐"),
                new DeviceList("", "", null, 1, "窗户"),
                new DeviceList("", "", null, 2, "窗户"),
                new DeviceList("", "", "1", 3, "窗户"),
                new DeviceList("", "", "1", 4, "窗户"),
                new DeviceList("", "", "0", 5, "窗户"),
                new DeviceList("", "", "0", 6, "窗户"),

//                new DeviceList("dl_2", "", "0", 2, "音乐"),
//                new DeviceList("dl_3", "", "1", 3, "窗户"),
//                new DeviceList("dl_4", "", "0", 4, "新风"),
//                new DeviceList("dl_5", "", "0", 1, "净化"),
//                new DeviceList("dl_6", "", "0", 2, "灯光"),
//                new DeviceList("dl_7", "", "0", 3, "空调"),
//                new DeviceList("dl_8", "", "0", 4, "电视"),
//                new DeviceList("dl_9", "", "0", 1, "音乐"),
//                new DeviceList("dl_10", "", null, 2, "窗户"),
//                new DeviceList("dl_11", "", null, 3, "新风"),
//                new DeviceList("dl_12", "", "0", 4, "净化"),
//                new DeviceList("dl_13", "", "0", 1, "灯光"),
//                new DeviceList("dl_14", "", "0", 2, "空调"),
//                new DeviceList("dl_15", "", "0", 3, "电视"),
//                new DeviceList("dl_16", "", "0", 4, "音乐"),
//                new DeviceList("dl_17", "", null, 1, "窗户"),
//                new DeviceList("dl_18", "", "0", 2, "新风"),
//                new DeviceList("dl_19", "", "0", 3, "净化"),
//                new DeviceList("dl_20", "", "1", 4, "灯光"),
//                new DeviceList("dl_21", "", "1", 1, "空调"),
//                new DeviceList("dl_22", "", "0", 2, "电视"),
//                new DeviceList("dl_23", "", "1", 3, "音乐"),
//                new DeviceList("dl_24", "", "0", 4, "窗户"),
//                new DeviceList("dl_25", "", "1", 1, "新风"),
//                new DeviceList("dl_26", "", "0", 2, "净化"),
//                new DeviceList("dl_27", "", "0", 3, "灯光"),
//                new DeviceList("dl_28", "", "0", 4, "空调"),
//                new DeviceList("dl_52", "", "0", 5, "洗衣机"),
//                new DeviceList("dl_53", "", "0", 6, "扫地机")
        });

        int[] roomImages = new int[]{
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };
        int[] deviceTypeImages = new int[]{
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };
        zoneView.setRoomInfos(roomInfos);
        zoneView.setDeviceTypes(deviceTypes);
        zoneView.setDeviceLists(deviceLists);
        zoneView.setRoomImages(roomImages);
        zoneView.setDeviceTypeImages(deviceTypeImages);

        //刷新界面
        zoneView.refresh();

        zoneView.setOnZoneClickListener(new ZoneView.OnZoneClickListener() {
            @Override
            public void onDeviceClick(DeviceList deviceList, Boolean sel) {
                Toast.makeText(ZoneViewTestActivity.this, "选中了设备 房间号：" + deviceList.getRoomId() + "  设备：" + deviceList.getDevType() + "," +
                                "是否选中：" + sel,
                        Toast
                                .LENGTH_SHORT).show();
            }

            @Override
            public void onHouseClick(RoomInfo roomInfo, boolean b) {
                Toast.makeText(ZoneViewTestActivity.this, "房间：" + roomInfo.getRoomId() + ",是否选中：" + b, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceMulClick(DeviceType deviceType, boolean b) {
                Toast.makeText(ZoneViewTestActivity.this, "设备：" + deviceType.getName() + ",是否选中：" + b, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
