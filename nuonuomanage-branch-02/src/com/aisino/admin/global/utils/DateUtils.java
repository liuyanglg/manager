package com.aisino.admin.global.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	private static SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String format(Date date){
		return defaultFormat.format(date);
	}
	
	public static String format(Date date,String format){
		String rtn = null;
		if(format!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			rtn = sdf.format(date);
		}
		return rtn;
	}
	
	public static String format(String timastamp){
		Date date = new Date(Long.parseLong(timastamp));
		return format(date);
	}
	
	public static Date parse(String dateStr){
		try {
			return defaultFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parse(String dateSrt,String formatStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			return format.parse(dateSrt);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static String  formatSting(String str){
		if(str==null){
			return "";
		}
		return str;
	}
	
	public static Date formatToDate(String str){
		if(str.length()<=10){
			return parse(str,"yyyy-MM-dd");
		}else{
			return parse(str,"yyyy-MM-dd HH:mm:ss");
		}
		
	}
	
	
	/**
	 * 获取指定格式的日期：当前时间向前多少天，或者向后多少天的日期
	 * @param format（日期展示格式）
	 * @param num（正数的时候是向后的日期，负数为向前的日期）
	 */
	public static String 	dateBeforeOrAfter(String format ,Integer num){
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.add(Calendar.DAY_OF_MONTH, num);
		if(format!=null){
			return format(calendar.getTime(), format);
		}
		return format(calendar.getTime());
	}
	
	public static Date dateBeforeOrAfter(Integer num){
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}
	
	public static Date dateBeforeOrAfter(Date date,Integer num){
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}
	
	public static Date todayEnd(){
		Date date = new Date();
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date;
	}
	
	public static Date dayEnd(Date date){
		if(date!=null){
			date.setHours(23);
			date.setMinutes(59);
			date.setSeconds(59);
		}
		return date;
	}
}
