package com.kc.tool;

/**
 * Created by Administrator on 2017/4/12 0012.
 * <p/>
 * /**
 * !!!!!!!!
 * list或vector容器若直接使用add方法，会添加重复的对象。
 * <p/>
 * 因此，放入对象前必须判断列表里是否已添加。
 */
public class SubscriberManager {

    private static final String TAG = "SubscriberManager";

    /**
     * 实现设备更新接口
     */
    public void subscribe(Object obj) {
    }

    public void releaseSub(Object obj) {
    }

    private void clearInterface(Object object, Object listObj) {
    }

}
