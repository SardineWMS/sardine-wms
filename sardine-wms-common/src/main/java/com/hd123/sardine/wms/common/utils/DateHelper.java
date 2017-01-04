/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	DateHelper.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间帮助类
 *
 * @author zhangsai
 */
public class DateHelper extends org.apache.commons.lang3.time.DateUtils {

    /** 时间格式化：yyyy-MM-dd HH:mm:ss */
    public static SimpleDateFormat FMT_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 时间格式化：yyyy-MM-dd */
    public static SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");

    /** 时间格式化：MM/dd/yyyy HH:mm:ss */
    public static SimpleDateFormat FMT_MDYHMS = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /** 时间格式化：MM/dd/yyyy */
    public static SimpleDateFormat FMT_MDY = new SimpleDateFormat("MM/dd/yyyy");

    public static Date strToDateTime(String s) {
        Date date = null;
        try {
            date = FMT_YMDHMS.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strToDate(String s) {
        Date date = null;
        try {
            date = FMT_YMD.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateTimeToStr(Date date) {
        if (date == null)
            return null;
        return FMT_YMDHMS.format(date);
    }

    public static String DateToStr(Date date) {
        return FMT_YMD.format(date);
    }
}
