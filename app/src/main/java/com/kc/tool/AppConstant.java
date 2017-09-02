package com.kc.tool;


public class AppConstant {

    private static final String TAG = "AppConstant";

    //            public static final String IP_LAN = "192.168.1.38";//实验室服务端
//    public static final String IP_LAN = "192.168.124.208";//实验室服务端
    public static final String IP_LAN = "119.23.61.190";//云端服务
//    public static final String IP_LAN = "103.44.145.248";//实验室本地,花生壳IP

    public static final int PORT_LAN = 9002;//办公室，云端
//    public static final int PORT_LAN = 14957;//花生壳端口

    public static final String IP_CLOUD = "139.196.52.159";//云端服务端
    public static final int PORT_CLOUD = 10002;//云端服务端
//    public static final String IP_LAN = "mjlab.xicp.cn";//实验室服务端
//    public static final int PORT_LAN = 17499;//实验室服务端


    public static boolean hasUserForcedOffline = false;// 用户是否被挤掉
    //默认值
    /*空调相关（可作为最低温，最高温判断范围）：

    制热模式：温度范围16℃~30℃
    送风模式：温度固定24℃，不可调节温度
    制冷模式：温度范围18℃~30℃
    除湿模式：温度范围18℃~30℃
    自动模式：温度范围18℃~30℃
    */
    public static final int JURISDICTION_NUM = 4;//未分配设备的房间id
    public static final int DEFAULT_UNALLOCATED_ROOM_ID = 0;//未分配设备的房间id
    public static final int MAX_AIR_TEMP = 30;
    public static final int MIN_AIR_TEMP = 16;
    public static final int MAX_FLOOR_H_TEMP = 35;
    public static final int MIN_FLOOR_H_TEMP = 5;
    public static final int MAX_PLAYER_VOLUME = 15;
    public static final int MIN_PLAYER_VOLUME = 0;
    public static final int INTERVAL_OPERATION_LIMITED = 600;//MS

    //
    public static final String STR_DEFAULT = "--";//
    public static final String STR_DATA = "data";//
    public static final String STR_CODE = "code";//
    public static final String STR_COLON = "：";//

    // SharedPreferences用户信息字段
    public static final String SP_JUR = "jurisdiction";
    public static final String SP_SESSION_ID = "session_id";
    //    public static final String SP_USER_NAME = "user_name";
    public static final String SP_USER_PHONE = "user_phone";
    public static final String SP_USER_PSW = "user_psw";
    public static final String SP_LOGIN_SCS = "login_scs";
    public static final String SP_DOORBELL_ID = "doorbell_id";// 门铃设备ID
    public static final String SP_CONFIG_SECURITY_VOICE = "CONFIG_SECURITY_VOICE";//安防配置声音
    public static final String SP_CONFIG_SECURITY_SHAKE = "CONFIG_SECURITY_SHAKE";//安防配置震动
    //    public static final String SP_USER_IMGPATH = "user_imgpath";
    public static final String SP_IS_FIRST_IN = "is_first_in";// 用户是否安装后第一次打开应用
    public static final String SP_SELECTED_DEFEND = "selected_defend";// 用户历史选择撤or布防
    public static final String SP_USER_SET_LANGUAGE = "SP_USER_SET_LANGUAGE";//
//    public static final String SP_USER_IGNORE_UPDATE = "SP_USER_IGNORE_UPDATE";//用户是否忽视更新

    // 添加场景 名字和id
    public static final String DB_TABLE_SCENE = "scene";
    public static final String DB_SCENE_SCENE_ID = "scene_id";
    public static final String DB_SCENE_SCENE_NAME = "scene_name";
    // 场景 任务执行类
    public static final String DB_TABLE_SCENE_ACTION = "scene_action";
    public static final String DB_SCENE_ACTION_SCENE_ID = "scene_id";
    public static final String DB_SCENE_ACTION_COMMAND_STR = "command_str";
    public static final String DB_SCENE_ACTION_DEVICE_ID = "device_id";
    public static final String DB_SCENE_ACTION_ORDER_S = "order_s";
    public static final String DB_SCENE_ACTION_DATA = "data";
    public static final String DB_SCENE_ACTION_TYPE = "type";

    // 设备类型,以下也作为返回数据中的参数名
    public static final String TYPE_AIRBOX = "airbox";
    public static final String TYPE_LAMP = "lamp";
    public static final String TYPE_CURTAIN = "windown";
    public static final String TYPE_MUSIC_PLAYER = "hope";
    public static final String TYPE_NATHER = "nather";
    public static final String TYPE_FLOOR_H = "veiga";
    public static final String TYPE_DOOR_LOCK = "door";
    public static final int DEVICE_NUM = 7;

    //请求值
    public static final Integer CODE_HEART = 0x00;//用户操作数据信息标识
    public static final String ACT_CHANGE = "change";

    /**
     * 仅用来区分返回和发送的数据类型或动作，不能作为分发到应用中的code值
     * 否则会和API类中的通知code冲突。
     */
    public static final int ACT_USER_LOGIN = 0X00;
    public static final int ACT_USER_RELOGIN = 0X01;
    public static final int ACT_USER_GET_VERIFY_CODE = 0X02;
    public static final int ACT_USER_FORGET_PASS = 0X03;
    public static final int ACT_USER_CHANGE_PASS = 0X20;
    public static final int ACT_USER_REGISTER = 0X04;
    public static final int ACT_USER_CHECK_PHONE = 0X05;
    public static final int ACT_USER_GET_FORGET_CODE = 0X06;
    public static final int ACT_USER_GET_DEVICE_LIST = 0X10;
    public static final int ACT_USER_GET_SINGIMG = 0x11;
    public static final int ACT_USER_GET_SONG_LIST = 0x13;
    public static final int ACT_USER_CHANGE_SINGIMG = 0x22;
    public static final int ACT_USER_GET_DEVICE_INFO = 0X12;
    public static final int ACT_USER_GET_USERLIST_INFO = 0X14;
    public static final int ACT_USER_GET_LOG = 0X15;
    public static final int ACT_USER_CHANGE_NAME = 0X21;
    public static final int ACT_USER_CHANGE_JUR = 0X23;
    public static final int ACT_USER_DETELE_SUNACCOUNT = 0X30;//删除子账户
    public static final int ACT_DETELE_ROOM = 0X31;//删除房间
    public static final int ACT_CHANGE_ROOM_NAME = 0X25;//修改房间名称
    public static final int ACT_CHANGE_DEVICE_NAME = 0X24;//修改设备名称
    public static final int ACT_CHANGE_ROOM_OF_DEVICE = 0X26;//修改设备归属房间
    public static final int ACT_ADD_ROOM = 0X27;//增加房间

    //捷控  定时 的ACT
    public static final int ACT_CONVENIENT_ADD = 0X00;
    public static final int ACT_CONVENIENT_START = 0X01;
    public static final int ACT_CONVENIENT_CHANGE = 0X10;//修改捷控信息
    public static final int ACT_CONVENIENT_TASK_CHANGE = 0X11;//新建 修改执行类信息
    public static final int ACT_CONVENIENT_DETELE = 0X21;//删除捷控
    public static final int ACT_CONVENIENT_TASK_DETELE = 0X20;//删除执行类

    public static final int ACT_CONVENIENT_ALLCONVENIENT = 0X30;//读取所有捷控
    public static final int ACT_TIMING_ADD = 1;//定时新建
    public static final int ACT_TIMING_START = 5;//定时开关
    public static final int ACT_TIMING_CHANGE = 2;//修改定时信息
    public static final int ACT_TIMING_DETELE = 3;//删除定时
    public static final int ACT_TIMING_ALLTIMING = 4;//读取所有定时

    // 5个指数的单位
    public static final String UNIT_OXYGEN = "PPM";// 42
    public static final String UNIT_CLEAN = "ug/m3";// 44
    public static final String UNIT_LIGHT = "LUX";// 19
    public static final String UNIT_WET = "%";// 17
    public static final String UNIT_TEMPER = "℃";// 17

    //活动之间常见传参key
    public static final String KEY_DEVICE_ID = "device_id";//
    public static final String KEY_ROOM_ID = "room_id";//
    public static final String KEY_FRAGMENT_INDEX = "fragment_index";//

    // key
    public static final String KEY_DIALOG_OLD_NAME = "dialog_old_name";
    public static final String KEY_DIALOG_TITLE = "dialog_msg";
    public static final String KEY_DIALOG_DESC = "dialog_desc";
    public static final String KEY_DIALOG_CODE = "dialog_CODE";
    public static final String KEY_DIALOG_CANCELABLE = "dialog_cancelable";
    public static final String KEY_DIALOG_OLD_AS_HINT = "KEY_DIALOG_OLD_AS_HINT";

    //发送命令常用的固定值
    public static final String VALUE_POWER = "power";
    public static final String VALUE_ON = "on";
    public static final String VALUE_OFF = "off";
    public static final String VALUE_STOP = "stop";

    //权限等级
    public static final int JUR_MAIN = 0;
    public static final int JUR_CTRL = 1;
    public static final int JUR_VIEW = 2;
    public static final int JUR_NONE = 3;
    //权限等级称呼
    public static final String JUR_NAME_BOSS = "家长：";
    public static final String JUR_NAME_CTRL = "家人：";
    public static final String JUR_NAME_VIEW = "客人：";
    public static final String JUR_NAME_NONE = "陌生人：";

}
