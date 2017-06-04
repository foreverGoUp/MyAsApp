package com.kc.util;


import com.kc.bean.DeviceList;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
public class ComparatorIml implements Comparator<DeviceList> {
    @Override
    public int compare(DeviceList one, DeviceList two) {
//        Collator ca = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
        int flag = 0;
//        if (ca.compare(one,two) < 0) {
//            flag = -1;
//        }
//        else if(ca.compare(one,two) > 0) {
//            flag = 1;
//        }
//        else {
//            flag = 0;
//        }
        String py1 = CharacterParser.getInstance().getSelling(one.getName());
        String py2 = CharacterParser.getInstance().getSelling(two.getName());
        flag = py1.compareToIgnoreCase(py2);
//        flag = ca.compare(py1, py2);
//        Log.e("TEST", "compare " + py1 + ":" + py2 + " flag=" + flag);
        return flag;
    }

}
