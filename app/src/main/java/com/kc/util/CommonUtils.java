package com.kc.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;


/*
 * 常用工具类
 * 
 * 
 * */

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    private static Toast sToast;
    private static long sLastToastShowTime;
    private static String sLastToastShowText;
    public static void showToast(Context context,
                                 String content) {
        if (content == null) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        //如果2次文本相同且显示间隔小于Toast.LENGTH_SHORT（2s）,则不显示
        if (sLastToastShowText != null && sLastToastShowText.equals(content)) {
            if (System.currentTimeMillis() - sLastToastShowTime < 2000) {
                return;
            }
        }
        sToast.setText(content);
        //设置显示时长
        if (content.length() > 10) {
            sToast.setDuration(Toast.LENGTH_LONG);
        } else {
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();

        //记录
        sLastToastShowTime = System.currentTimeMillis();
        sLastToastShowText = content;
    }

    /*
     *
     * 更新listview指定行
     *
     * @param listview
     * @param adapter
     * @param row
     *
     * */
    public static void updateListSingleRow(AbsListView listView, BaseAdapter adapter, int row) {
        if (listView.getAdapter() == adapter) {
            int start = listView.getFirstVisiblePosition();
            int last = listView.getLastVisiblePosition();
            if (row >= start && row <= last) {
                View view1 = listView.getChildAt(row - start);
                adapter.getView(row, view1, listView);
                Log.i(TAG, "更新了：start=" + start + ",last=" + last + ",row=" + row);
            }
        }
    }

    /*
     *
     * 更新可侧滑删除Listview指定行
     *
     * @param listview
     * @param adapter
     * @param row
     *
     * */
    public static boolean isVisible(AbsListView listView, int row) {
        int start = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        if (row >= start && row <= last) {
            return true;
        } else {
            return false;
        }
    }

//    public static void setTopImage(TextView textView, int imageId, int dp) {
//        Drawable drawable = JYApplication.getContext().getResources().getDrawable(imageId);
//        int width = SizeUtils.dp2px(JYApplication.getContext(), dp);
//        drawable.setBounds(0, 0, width, width);
//        textView.setCompoundDrawables(null, drawable, null, null);
//    }
    /**
     * 查找某个对象在列表中的位置
     * */
//    public static int getPosInList(String id, List<DeviceList> list) {
//        int ret = -1;
//        int size = list.size();
//        DeviceList dev = null;
//        for (int i = 0; i < size; i++) {
//            dev = list.get(i);
//            if (dev.getId().equals(id)) {
//                ret = i;
//                break;
//            }
//        }
//        return ret;
//    }

    /**
     * 对某个对象的列表按照对象的名称字段排序
     * */
//    public static void sortList(List<DeviceList> list, List<DeviceList> doorLocklist) {
//        com.jy.util.Log.e(TAG, "对设备名称排序开始：start time=" + System.currentTimeMillis());
//        List<DeviceList> sortedList = new ArrayList<>();
//        List<DeviceList> sortedDoorLockList = new ArrayList<>();
//        for (int i = 0; i < AppConstant.DEVICE_NUM; i++) {
//            if (list == null && i + 1 < 7) {
//                continue;
//            }
//            switch (i + 1) {
//                case 1:
//                    sortedList.addAll(getSortedListByType(list, AppConstant.TYPE_LAMP));
//                    break;
//                case 2:
//                    sortedList.addAll(getSortedListByType(list, AppConstant.TYPE_CURTAIN));
//                    break;
//                case 3:
//                    sortedList.addAll(getSortedListByType(list, AppConstant.TYPE_AIRBOX));
//                    break;
//                case 4:
//                    sortedList.addAll(getSortedListByType(list, AppConstant.TYPE_MUSIC_PLAYER));
//                    break;
//                case 5:
//                    sortedList.addAll(getSortedListByType(list, AppConstant.TYPE_NATHER));
//                    break;
//                case 6:
//                    sortedList.addAll(getSortedListByType(list, AppConstant.TYPE_FLOOR_H));
//                    break;
//                case 7:
//                    if (doorLocklist == null) {
//                        continue;
//                    }
//                    sortedDoorLockList.addAll(getSortedListByType(doorLocklist, AppConstant.TYPE_DOOR_LOCK));
//                    break;
//            }
//        }
//        if (list != null) {
//            list.clear();
//            list.addAll(sortedList);
//        }
//        if (doorLocklist != null) {
//            doorLocklist.clear();
//            doorLocklist.addAll(sortedDoorLockList);
//        }
//        com.jy.util.Log.e(TAG, "对设备名称排序结束：end time=" + System.currentTimeMillis());
//    }
//

//    private static List<DeviceList> getSortedListByType(List<DeviceList> list, String type) {
//        int size = list.size();
//        List<DeviceList> listByType = new ArrayList<>();
//        if (!type.equals(AppConstant.TYPE_DOOR_LOCK)) {
//            for (int i = 0; i < size; i++) {
//                if (list.get(i).getType().equals(type)) {
//                    listByType.add(list.get(i));
//                }
//            }
//        } else {
//            listByType.addAll(list);
//        }
//
//        size = listByType.size();
//        DeviceList[] arr = new DeviceList[size];
//        for (int i = 0; i < size; i++) {
//            arr[i] = listByType.get(i);
//        }
//
//
//        Arrays.sort(arr, 0, size, new ComparatorIml());
////        for (int i = 0; i < arr.length; i++) {
////            com.jy.util.Log.e(TAG, "arr i="+i+"type="+arr[i].getType()+",value="+arr[i].getName());
////        }
//        listByType.clear();
//        for (int i = 0; i < size; i++) {
//            listByType.add(arr[i]);
//        }
//        return listByType;
//    }
}
