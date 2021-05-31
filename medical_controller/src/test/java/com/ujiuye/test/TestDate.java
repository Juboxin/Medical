package com.ujiuye.test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author JuBoxin
 * @date 2021/5/26 - 21:42
 */
public class TestDate {
    public static void main(String[] args) {
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println(time);
        Long time1 = time - 60 * 60 * 24;
        Date date = new Date(time1);
        System.out.println("==========" + date);


    }
}
