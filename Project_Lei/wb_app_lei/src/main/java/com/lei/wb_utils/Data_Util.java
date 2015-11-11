package com.lei.wb_utils;

import java.util.HashMap;
/** 日期转换工具 */
public class Data_Util {
    public static String getData(String time){
        HashMap<String,String> month = new HashMap<>();
        HashMap<String,String> week = new HashMap<>();
        month.put("Jan","1");
        month.put("Feb","2");
        month.put("Mar","3");
        month.put("Apr","4");
        month.put("May","5");
        month.put("Jan","6");
        month.put("Jul","7");
        month.put("Aug","8");
        month.put("Sep","9");
        month.put("Oct","10");
        month.put("Nov","11");
        month.put("Dec","12");
        week.put("Mon","一");
        week.put("Tue","二");
        week.put("Wed","三");
        week.put("Thu","四");
        week.put("Fri","五");
        week.put("Sat", "六");
        week.put("Sun","天");
        String[] strs = time.split(" ");
        String data = strs[5]+"-";
        data = data+month.get(strs[1])+"-"+strs[2]+" "+strs[3];
        String _week = "星期"+week.get(strs[0]);
        data = _week + " " + data ;
        return data;
    }
}
