package com.kc.util;

//import com.jy.bean.fastctrl.Convenient;
//import com.jy.data.DataCenter;onvenient;
//import com.jy.data.DataCenter

import android.content.Context;
import android.widget.Toast;

import com.jy.R;
import com.jy.bean.convenient.Convenient;
import com.jy.data.DataCenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/12/16 0016.
 */

public class EidtTimeTool {

    static EidtTimeTool eidtTimeTool;
    private StringBuffer strfastctrlName;
    private StringBuffer strfastctrlIds;
    private StringBuffer strfastctrlId;

    public static EidtTimeTool getInstance() {
        if (eidtTimeTool == null) {
            eidtTimeTool = new EidtTimeTool();
        }
        return eidtTimeTool;
    }

    //将数字型转换成中文型(排序版)
    public String getSortDay(String sort) {
        String[] split = sort.split(",");
        String sorCustomDay = getSorCustomDay(split);
        return sorCustomDay;
    }

    //将数字型转换成中文型
    public String getDay(String sort) {

        StringBuffer strday = new StringBuffer();
        String[] split = sort.split(",");
        for (int i = 0; i < split.length; i++) {
            String string = split[i];
            int day = Integer.parseInt(string);
            switch (day) {
                case 1:
                    String mon = JYApplication.getResString(R.string.edttime_mon);
                    if (strday.length() > 0) {
                        strday.append("," + mon);

                    } else {
                        strday.append(mon);
                    }
                    break;
                case 2:
                    String tue = JYApplication.getResString(R.string.edttime_tue);
                    if (strday.length() > 0) {
                        strday.append("," + tue);

                    } else {
                        strday.append(tue);
                    }
                    break;
                case 3:
                    String wed = JYApplication.getResString(R.string.edttime_wed);
                    if (strday.length() > 0) {
                        strday.append("," + wed);

                    } else {
                        strday.append(wed);
                    }
                    break;
                case 4:
                    String thu = JYApplication.getResString(R.string.edttime_thu);
                    if (strday.length() > 0) {
                        strday.append("," + thu);

                    } else {
                        strday.append(thu);
                    }
                    break;
                case 5:
                    String fri = JYApplication.getResString(R.string.edttime_fri);
                    if (strday.length() > 0) {
                        strday.append("," + fri);

                    } else {
                        strday.append(fri);
                    }
                    break;
                case 6:
                    String sat = JYApplication.getResString(R.string.edttime_sat);
                    if (strday.length() > 0) {
                        strday.append("," + sat);

                    } else {
                        strday.append(sat);
                    }
                    break;
                case 7:
                    String sun = JYApplication.getResString(R.string.edttime_sun);
                    if (strday.length() > 0) {
                        strday.append("," + sun);

                    } else {
                        strday.append(sun);
                    }
                    break;

                default:
                    break;
            }


        }
        return strday.toString();

    }

    //将数字型转换成中文型(排序)
    public String getSorCustomDay(String[] dataweeks) {
        int[] days = new int[dataweeks.length];
        for (int i = 0; i < dataweeks.length; i++) {
            String string = dataweeks[i];
            int week = Integer.parseInt(string);
            days[i] = week;
        }
        Arrays.sort(days); //进行排序
        StringBuffer strday = new StringBuffer();

        for (int i = 0; i < days.length; i++) {
            int day = days[i];
            switch (day) {
                case 1:
                    String mon = JYApplication.getResString(R.string.edttime_mon);
                    if (strday.length() > 0) {
                        strday.append("," + mon);

                    } else {
                        strday.append(mon);
                    }
                    break;
                case 2:
                    String tue = JYApplication.getResString(R.string.edttime_tue);
                    if (strday.length() > 0) {
                        strday.append("," + tue);

                    } else {
                        strday.append(tue);
                    }
                    break;
                case 3:
                    String wed = JYApplication.getResString(R.string.edttime_wed);
                    if (strday.length() > 0) {
                        strday.append("," + wed);

                    } else {
                        strday.append(wed);
                    }
                    break;
                case 4:
                    String thu = JYApplication.getResString(R.string.edttime_thu);
                    if (strday.length() > 0) {
                        strday.append("," + thu);

                    } else {
                        strday.append(thu);
                    }
                    break;
                case 5:
                    String fri = JYApplication.getResString(R.string.edttime_fri);
                    if (strday.length() > 0) {
                        strday.append("," + fri);

                    } else {
                        strday.append(fri);
                    }
                    break;
                case 6:
                    String sat = JYApplication.getResString(R.string.edttime_sat);
                    if (strday.length() > 0) {
                        strday.append("," + sat);

                    } else {
                        strday.append(sat);
                    }
                    break;
                case 7:
                    String sun = JYApplication.getResString(R.string.edttime_sun);
                    if (strday.length() > 0) {
                        strday.append("," + sun);

                    } else {
                        strday.append(sun);
                    }
                    break;

                default:
                    break;
            }

        }
        return strday.toString();

    }

    //将数字型转换成中文型
    public String getCustomDay(String[] dataDays) {
        StringBuffer strday = new StringBuffer();
        for (int i = 0; i < dataDays.length; i++) {
            String string = dataDays[i];
            if (string != null) {

                int day = Integer.parseInt(string);
                switch (day) {
                    case 1:
                        String mon = JYApplication.getResString(R.string.edttime_mon);
                        if (strday.length() > 0) {
                            strday.append("," + mon);

                        } else {
                            strday.append(mon);
                        }
                        break;
                    case 2:
                        String tue = JYApplication.getResString(R.string.edttime_tue);
                        if (strday.length() > 0) {
                            strday.append("," + tue);

                        } else {
                            strday.append(tue);
                        }
                        break;
                    case 3:
                        String wed = JYApplication.getResString(R.string.edttime_wed);
                        if (strday.length() > 0) {
                            strday.append("," + wed);

                        } else {
                            strday.append(wed);
                        }
                        break;
                    case 4:
                        String thu = JYApplication.getResString(R.string.edttime_thu);
                        if (strday.length() > 0) {
                            strday.append("," + thu);

                        } else {
                            strday.append(thu);
                        }
                        break;
                    case 5:
                        String fri = JYApplication.getResString(R.string.edttime_fri);
                        if (strday.length() > 0) {
                            strday.append("," + fri);

                        } else {
                            strday.append(fri);
                        }
                        break;
                    case 6:
                        String sat = JYApplication.getResString(R.string.edttime_sat);
                        if (strday.length() > 0) {
                            strday.append("," + sat);

                        } else {
                            strday.append(sat);
                        }
                        break;
                    case 7:
                        String sun = JYApplication.getResString(R.string.edttime_sun);
                        if (strday.length() > 0) {
                            strday.append("," + sun);

                        } else {
                            strday.append(sun);
                        }
                        break;

                    default:
                        break;
                }

            }
        }
        return strday.toString();

    }

    public String sHour(int hour) {
        String shour;
        if (hour < 10) {
            shour = 0 + "" + hour + "";
        } else {
            shour = hour + "";

        }
        return shour;
    }

    public String sMin(int min) {
        String smin;
        if (min < 10) {
            smin = 0 + "" + min + "";
        } else {
            smin = +min + "";

        }
        return smin;
    }

    public String convenientName(String name) {
        StringBuffer strname = new StringBuffer();

        if (strname.length() > 0) {
            strname.append("," + strname);

        } else {
            strname.append(strname);
        }
        return strname.toString();
    }

    public String getConvenientId(ArrayList<Convenient> fastctrlArrayList) {//所选择de捷控的id
        StringBuffer strid = new StringBuffer();
        for (int i = 0; i < fastctrlArrayList.size(); i++) {
            Convenient convenient = fastctrlArrayList.get(i);
            if (convenient.getCount() == 1) {
                Integer sceneId = convenient.getId();
                if (strid.length() > 0) {
                    strid.append("," + sceneId);

                } else {
                    strid.append(sceneId);
                }
            }

        }
        return strid.toString();
    }

    public ConcurrentHashMap<Integer, Convenient> getConvenienToMap(String conIds, ConcurrentHashMap<Integer, Convenient> convenientMapVector) {
        Vector<Convenient> fastCtrlListVector = DataCenter.getInstance().getFastCtrlListVector();
        if (conIds == null || conIds.equals("")) {
            return convenientMapVector;

        }
        String[] split = conIds.split(",");
        for (int i = 0; i < split.length; i++) {
            String id = split[i];
            int intId = Integer.parseInt(id);
            for (int j = 0; j < fastCtrlListVector.size(); j++) {
                Convenient convenient = fastCtrlListVector.get(j);
                Integer convenientId = convenient.getId();
                if (intId == convenientId) {
                    convenientMapVector.put(convenientId, convenient);
                }
            }
        }
        return convenientMapVector;
    }

    public String getConvenientNames(String conIds) {
        ConcurrentHashMap<Integer, String> fastCtrlNameMapVector = DataCenter.getInstance().getFastCtrlNameMapVector();
        StringBuffer strname = new StringBuffer();
        if (conIds == null || conIds.equals("")) {
            return strname.toString();
        }
        String[] split = conIds.split(",");
        int[] convenientIds = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            String id = split[i];
            if (id == null || id.equals("")) {
                continue;
            }
            Integer intId = Integer.parseInt(id);
            convenientIds[i] = intId;
        }
        Arrays.sort(convenientIds); //进行排序
        for (int i = 0; i < convenientIds.length; i++) {
            int intId1 = convenientIds[i];
            String name = fastCtrlNameMapVector.get(intId1);
            if (name != null) {
                if (strname.length() > 0) {
                    strname.append("," + name);
                } else {
                    strname.append(name);
                }
            }
        }
        return strname.toString();
    }

    //    private ArrayList<Integer> qsort_asc(Set<Integer> data, int low, int high) {//用来排序选中 的捷控
//
//        int i, j, x;
//        ArrayList<Integer> ArrayList=new ArrayList<Integer>(data);
//        if (low < high) { // 这个条件用来结束递归
//
//            i = low;
//
//            j = high;
//
//            x = ArrayList.get(i);
//
//            while (i < j) {
//
//                while (i < j && ArrayList.get(j) > x) {
//
//                    j--; // 从右向左找第一个小于x的数
//
//                }
//
//                if (i < j) {
//
//                    ArrayList.set(i,ArrayList.get(j));
//
//                    i++;
//
//                }
//
//                while (i < j && ArrayList.get(i) < x) {
//
//                    i++; // 从左向右找第一个大于x的数
//
//                }
//
//                if (i < j) {
//
//                    ArrayList.set(j,ArrayList.get(i));
//
//                    j--;
//
//                }
//
//            }
//
//            ArrayList.set(i,x);
//
//            qsort_asc(data, low, i - 1);
//
//            qsort_asc(data, i + 1, high);
//
//        }
//            return ArrayList;
//    }
    public String getShowConvenientNames() {
        ConcurrentHashMap<Integer, Convenient> convenientMapVector = DataCenter.getInstance().getConvenientMapVector();
        strfastctrlName = new StringBuffer();
        strfastctrlId = new StringBuffer();
        if (convenientMapVector != null && convenientMapVector.size() > 0) {
            int[] convenientIds = new int[convenientMapVector.size()];
            int i = 0;
            Iterator iter = convenientMapVector.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Convenient fastctrl = (Convenient) entry.getValue();
                Integer id = fastctrl.getId();
                convenientIds[i] = id;
                i++;
            }
            Arrays.sort(convenientIds); //进行排序
            for (int j = 0; j < convenientIds.length; j++) {
                int t = convenientIds[j];
                Convenient convenient = convenientMapVector.get(t);
                String name = convenient.getName();
                if (strfastctrlName.length() > 0) {
                    strfastctrlName.append("," + name);
                } else {
                    strfastctrlName.append(name);
                }
            }
//            Iterator iter = convenientMapVector.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                Convenient fastctrl = (Convenient) entry.getValue();
//            if (strfastctrlName.length() > 0) {
//                strfastctrlName.append("," + convenient.getName());
//            } else {
//                strfastctrlName.append(convenient.getName());
//            }
//            }
        }

        return strfastctrlName.toString();
    }

    public String getShowConvenientIds() {
        ConcurrentHashMap<Integer, Convenient> convenientMapVector = DataCenter.getInstance().getConvenientMapVector();
        strfastctrlIds = new StringBuffer();
        if (convenientMapVector != null && convenientMapVector.size() > 0) {
            Iterator iter = convenientMapVector.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Convenient fastctrl = (Convenient) entry.getValue();
                if (strfastctrlIds.length() > 0) {
                    strfastctrlIds.append("," + fastctrl.getId());
                } else {
                    strfastctrlIds.append(fastctrl.getId());
                }
            }
        }

        return strfastctrlIds.toString();
    }

    public String getDeviceNames(String devId) {
        return DataCenter.getInstance().getDeviceNameMapVector().get(devId);
    }

    public boolean whetherRenamed(String name) {
        ConcurrentHashMap<Integer, String> fastCtrlNameMapVector = DataCenter.getInstance().getFastCtrlNameMapVector();
        if (fastCtrlNameMapVector != null && fastCtrlNameMapVector.size() > 0) {
            Iterator iter = fastCtrlNameMapVector.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String fastctrlName = (String) entry.getValue();
                if (fastctrlName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean sendAddFastctrlOrder(String name, Integer convenientimg, Context context) {
        if (name.equals("")) {
            String resString = JYApplication.getResString(R.string.convenient_no_name);
            Toast.makeText(context, resString, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            boolean b = EidtTimeTool.getInstance().whetherRenamed(name);
            if (b) {
                String resString = JYApplication.getResString(R.string.convenient_name_exit);
                Toast.makeText(context, resString, Toast.LENGTH_SHORT).show();
//                showToast("捷控名字不能重复");
                return false;
            }
        }
        return true;
    }

    public boolean sendChangeFastctrlOrder(String rename, Integer convenientimg, Context context, Convenient convenient, String oldname) {
        if (rename.equals("")) {
            String resString = JYApplication.getResString(R.string.convenient_no_name);
            Toast.makeText(context, resString, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!oldname.equals(rename)) {
                boolean b = EidtTimeTool.getInstance().whetherRenamed(rename);
                if (b) {
                    String resString = JYApplication.getResString(R.string.convenient_name_exit);
                    Toast.makeText(context, resString, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    //    public ArrayList<Convenient> sortFastCtrl(List<Convenient> convenientList){
//        if (convenientList != null && convenientList.size() > 0) {
//            Set<Integer> integers = convenientList.keySet(
//            ArrayList<Integer> integers1 = this.qsort_asc(integers, 0, integers.size() - 1);
//            for (Integer Integer:integers1 ) {
//                Convenient convenient = convenientMapVector.get(Integer);
//                if (strfastctrlName.length() > 0) {
//                    strfastctrlName.append("," + convenient.getName());
//                } else {
//                    strfastctrlName.append(convenient.getName());
//                }
//            }
//
//        return  fastCtrlList;
//    }
    public int getConvenientImage(Integer img) {
        int ret = R.drawable.fastctrl_one;
        switch (img) {
            case 1:
                ret = R.drawable.fastctrl_one;
                break;
            case 2:
                ret = R.drawable.fastctrl_two;
                break;
            case 3:
                ret = R.drawable.fastctrl_three;
                break;
            case 4:
                ret = R.drawable.fastctrl_four;
                break;
        }
        return ret;
    }
}
