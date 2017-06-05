package com.kc.util;


import com.kc.bean.Command;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
public class ComparatorImlTask implements Comparator<Command> {
    @Override
    public int compare(Command one, Command two) {
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
        String py1 = CharacterParser.getInstance().getSelling(one.getDevName());
        String py2 = CharacterParser.getInstance().getSelling(two.getDevName());
        flag = py1.compareToIgnoreCase(py2);
//        flag = ca.compare(py1, py2);
//        Log.e("TEST", "compare " + py1 + ":" + py2 + " flag=" + flag);
        return flag;
    }

}
