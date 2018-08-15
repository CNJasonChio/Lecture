package com.jasonchio.lecture.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * /**
 * <p>
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━by:zhaoyaobang
 * <p>
 * Created by zhaoyaobang on 2018/7/7.
 */

public class TimeUtil {
	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long month = 31 * day;// 月
	private final static long year = 12 * month;// 年

	/**
	 * 返回文字描述的日期
	 *
	 * @param strTime
	 * @return
	 */
	public static String getTimeFormatText(String strTime) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date == null) {
			return null;
		}

		if (date.getTime() - new Date().getTime() > 0) {
			long diff = date.getTime() - new Date().getTime();
			long r = 0;

			if (diff > year) {
				r = (diff / year);
				return r + "年后";
			}
			if (diff > month) {
				r = (diff / month);
				return r + "个月后";
			}
			if (diff > day) {
				r = (diff / day);
				return r + "天后";
			}
			if (diff > hour) {
				r = (diff / hour);
				return r + "个小时后";
			}
			if (diff > minute) {
				r = (diff / minute);
				return r + "分钟后";
			}
			return "现在";

		} else {
			long diff = new Date().getTime() - date.getTime();
			long r = 0;
			if (diff > year) {
				r = (diff / year);
				return r + "年前";
			}
			if (diff > month) {
				r = (diff / month);
				return r + "个月前";
			}
			if (diff > day) {
				r = (diff / day);
				return r + "天前";
			}
			if (diff > hour) {
				r = (diff / hour);
				return r + "个小时前";
			}
			if (diff > minute) {
				r = (diff / minute);
				return r + "分钟前";
			}
			return "刚刚";
		}

	}
}


