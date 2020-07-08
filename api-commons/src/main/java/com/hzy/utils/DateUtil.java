package com.hzy.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
					"yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
					"yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");


	public static long getDateline() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 获取之后第几天的日期
	 *
	 * @param past
	 * @return
	 */
	public static long getAfterDateLong(int past) {
		Calendar calendar = Calendar.getInstance();
		long createTime = calendar.getTimeInMillis();
		//System.out.println(createTime);   //1541235267116
		SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String res = form1.format(createTime/1000);
		System.out.println(res);  //2018-11-03 16-54-27
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		System.out.println(calendar.getTimeInMillis()/1000);   //1541840067116
		return calendar.getTimeInMillis()/1000;
	}
	public static long getDateline(String date) {
		return (long) (toDate(date, "yyyy-MM-dd").getTime() / 1000);
	}

	public static long getDateHaveHour(String date) {
		return (long) (toDate(date, "yyyy-MM-dd HH").getTime() / 1000);
	}

	public static long getDateline(String date, String pattern) {
		return (long) (toDate(date, pattern).getTime() / 1000);
	}

	/**
	 * 将一个字符串转换成日期格式
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String date, String pattern) {
		if (("" + date).equals("")) {
			return null;
		}
		if (pattern == null) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date newDate = new Date();
		try {
			newDate = sdf.parse(date);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return newDate;
	}

	/**
	 * 将传入时间戳格式化成对应个数格式
	 *
	 * @param pattern 时间格式,null时默认yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDate(String pattern, Long time) {
		if (pattern == null || "".equals(pattern)) {
			pattern = "YYYY-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdfTime = new SimpleDateFormat(pattern);
		return sdfTime.format(new Date(time));
	}

	/**
	 * 根据传入格式获取当前时间
	 *
	 * @param pattern 时间格式,null时默认yyyy-MM-dd HH:mm
	 * @return
	 */
	public static String getDate(String pattern) {
		if (pattern == null || "".equals(pattern)) {
			pattern = "yyyy-MM-dd HH:mm";
		}
		SimpleDateFormat sdfTime = new SimpleDateFormat(pattern);
		return sdfTime.format(new Date());
	}

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @Title: compareDate
	 * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa = 0;
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		//System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到分钟后的日期
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterMINUTE(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.MINUTE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/**
	 * 时间戳转换成日期格式字符串
	 *
	 * @param seconds   精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 结束时间
	 *
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static Long getAfterDayDate1() throws ParseException {
		String day = DateUtil.getDay();
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day + " 23:59:59");
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);
		Date date1 = sdfd.parse(dateStr);
		long ts = date1.getTime() / 1000;
		return ts;
	}

	/**
	 * 开始时间
	 *
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static Long getAfterDayDate2() throws ParseException {
		String day = DateUtil.getDay();
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day + " 00:00:00");
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);
		Date date1 = sdfd.parse(dateStr);
		long ts = date1.getTime() / 1000;
		return ts;
	}

	/**
	 * TODO 描述:获取过去/未来days天时间戳(包括今天)
	 *
	 * @param days 多少天
	 * @param type 类别:true:过去day天;false:未来day天;
	 * @param sort 类别:true:正序;false:倒序;
	 * @return java.lang.Long[]
	 * @author Chensb
	 * @date 2019/10/29 9:28
	 */
	public static Long[] getDates(int days, boolean type, boolean sort) {
		Long[] dates = new Long[days];
		Long currentDate = getCurrentDate();
		for (int i = 0; i < days; i++) {
			if (type) {
				if (sort) {
					dates[i] = currentDate - 86400 * (days - i - 1);
				} else {
					dates[i] = currentDate - 86400 * (i);
				}
			} else {
				if (sort) {
					dates[i] = currentDate + 86400 * (i);
				} else {
					dates[i] = currentDate + 86400 * (days - i - 1);
				}
			}
		}
		return dates;
	}

	/**
	 * TODO 描述:获取今天凌晨00:00:00时间戳
	 *
	 * @param
	 * @return java.lang.Long
	 * @author Chensb
	 * @date 2019/10/29 9:35
	 */
	public static Long getCurrentDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime() / 1000;
	}

	public static void main(String[] args) {
		System.out.println(getDay());

		//System.out.println( getDate("",1584944660*1000l));

		// System.out.println(JSONArray.fromObject(getDates(7, true, true)).toString());
		// System.out.println(JSONArray.fromObject(getDates(7, true, false)).toString());
		// System.out.println(JSONArray.fromObject(getDates(7, false, true)).toString());
		// System.out.println(JSONArray.fromObject(getDates(7, false, false)).toString());
	}

}
