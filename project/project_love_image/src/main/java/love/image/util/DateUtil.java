package love.image.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class DateUtil {

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static String getNowDateShort() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return df.format(new Date());
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getTimeShort2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		if (null != strDate) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			Date strtodate = null;
			try {
				strtodate = formatter.parse(strDate);
			} catch (ParseException e) {
				System.out.println("格式转换失败----->" + strDate
						+ " 转换后的----------->"
						+ getNextDay2("1", "yyyy-MM-dd HH:mm"));
				strtodate = strToDateLong(getNextDay2("1", "yyyy-MM-dd HH:mm"));
				e.printStackTrace();
			}
			return strtodate;
		}
		return null;
	}

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(java.util.Date dateDate) {
		if (null != dateDate) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(dateDate);
			return dateString;
		}
		return "";
	}

	public static String ObjectToStrLong(Object dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null != dateDate) {
			String dateString = formatter.format(dateDate);
			return dateString;
		}
		return "";

	}

	public static String ObjectToDate(Object dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date StringToDate(String dateStr) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * 提取一个月中的最后一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 34 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return 字符串 yyyyMMddHHmmss
	 */
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return 字符串 yyyyMMddHHmmss
	 */
	public static String getDateStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 得到现在小时
	 */
	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	/**
	 * 得到现在分钟
	 * 
	 * @return
	 */
	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	/**
	 * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	 * 
	 * @param sformat
	 *            yyyyMMddhhmmss
	 * @return
	 */
	public static String getUserDate(String sformat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
	 */
	public static String getTwoHour(String st1, String st2) {
		String[] kk = null;
		String[] jj = null;
		kk = st1.split(":");
		jj = st2.split(":");
		if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
			return "0";
		else {
			double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
					/ 60;
			double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
					/ 60;
			if ((y - u) > 0)
				return y - u + "";
			else
				return "0";
		}
	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 时间前推或后推分钟,其中JJ表示分钟.
	 */
	public static String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	/**
	 * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 */
	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String mdate = "";
			Date d = strToDate(nowdate);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
					* 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 当前时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 */
	public static String getNextDay2(String delay, String formats) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(formats);
			String mdate = "";
			Date d = getNow();
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
					* 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断是否润年
	 * 
	 * @param ddate
	 * @return
	 */
	public static boolean isLeapYear(String ddate) {

		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		Date d = strToDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 返回美国时间格式 26 Apr 2006
	 * 
	 * @param str
	 * @return
	 */
	public static String getEDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(str, pos);
		String j = strtodate.toString();
		String[] k = j.split(" ");
		return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
	}

	/**
	 * 获取一个月的最后一天
	 * 
	 * @param dat
	 * @return
	 */
	public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
		String str = dat.substring(0, 8);
		String month = dat.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
				|| mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(dat)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}

	/**
	 * 判断二个时间是否在同一个周
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 * 
	 * @return
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
	 * 
	 * @param sdate
	 * @param num
	 * @return
	 */
	public static String getWeek(String sdate, String num) {
		// 再转换为时间
		Date dd = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1")) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	public static String getWeekStr(String sdate) {
		String str = "";
		str = DateUtil.getWeek(sdate);
		if ("1".equals(str)) {
			str = "星期日";
		} else if ("2".equals(str)) {
			str = "星期一";
		} else if ("3".equals(str)) {
			str = "星期二";
		} else if ("4".equals(str)) {
			str = "星期三";
		} else if ("5".equals(str)) {
			str = "星期四";
		} else if ("6".equals(str)) {
			str = "星期五";
		} else if ("7".equals(str)) {
			str = "星期六";
		}
		return str;
	}

	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
	 * 此函数返回该日历第一行星期日所在的日期
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getNowMonth(String sdate) {
		// 取该时间所在月的一号
		sdate = sdate.substring(0, 8) + "01";

		// 得到这个月的1号是星期几
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int u = c.get(Calendar.DAY_OF_WEEK);
		String newday = DateUtil.getNextDay(sdate, (1 - u) + "");
		return newday;
	}

	/**
	 * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
	 * 
	 * @param k
	 *            表示是取几位随机数，可以自己定
	 */

	public static String getNo(int k) {

		return getUserDate("yyyyMMddHHmmssSSS") + getRandom(k);
	}

	/**
	 * 生成格式为yyyymmddhhmmss
	 * 
	 * @param k
	 *            表示是取几位随机数，可以自己定
	 */

	public static String getNo() {

		return getUserDate("yyyyMMddhhmmss");
	}

	public static String getNo2() {

		return getUserDate("yyyyMMdd");
	}

	/**
	 * 返回一个随机数
	 * 
	 * @param i
	 * @return
	 */
	public static String getRandom(int i) {
		Random jjj = new Random();
		// int suiJiShu = jjj.nextInt(9);
		if (i == 0)
			return "";
		String jj = "";
		for (int k = 0; k < i; k++) {
			jj = jj + jjj.nextInt(9);
		}
		return jj;
	}

	/**
	 * 
	 * @param args
	 */
	public static boolean RightDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		;
		if (date == null)
			return false;
		if (date.length() > 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			sdf.parse(date);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	/**
	 * 比较当前时间和传入时间，当前时间小于传入时间返回true
	 */
	public static boolean compare_date(Date DATE2) {
		Date currentTime = new Date();
		try {
			if (currentTime.getTime() > DATE2.getTime()) {
				return false;
			} else if (currentTime.getTime() < DATE2.getTime()) {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 当前时间加几个小时
	 * 
	 * @return
	 */
	public static Date date_hour(Integer hour) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, hour * 30);
		return c.getTime();
	}

	/**
	 * 
	 */
	public static Date getNextDay_time(String nowdate, String delay) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mdate = "";
		Date d = strToDateLong(nowdate);
		long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60
				* 60;
		d.setTime(myTime * 1000);
		mdate = format.format(d);
		return StringToDate(mdate);
	}

	// 公共方法
	public static Date getNextDate(Date date, int dealy) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dealy);
		Date date1 = new Date(calendar.getTimeInMillis());
		return date1;
	}

	/**
	 * 当前时间加多少分钟
	 * 
	 * @return
	 */
	public static Date add_minute(Integer minute) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}

	public static Date firstSeconds(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static String firstSeconds2(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = null;
		try {
			d = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString = dateFormat.format(d);
		return dateString;
	}

	public static Date nextMonthFirstSeconds(String month) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		Date d2 = null;
		try {
			d = dateFormat.parse(month);
			System.out.println(d);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.MONTH, 1);
			d2 = c.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d2;
	}

	public static String getCurrentDateTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}

	public static String firstSeconds3(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString = dateFormat.format(d);
		return dateString;
	}

	/**
	 * 计算两个日期的时间差
	 * 
	 * @param formatTime1
	 * @param formatTime2
	 * @return
	 */
	public static boolean getTimeDifference(Date formatTime1, Date formatTime2) {
		SimpleDateFormat timeformat = new SimpleDateFormat(
				"yyyy-MM-dd,HH:mm:ss");
		long t1 = 0L;
		long t2 = 0L;
		try {
			t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1))
					.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2))
					.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 因为t1-t2得到的是毫秒级,所以要初3600000得出小时.算天数或秒同理
		int hours = (int) ((t1 - t2) / 3600000);
		int minutes = (int) (((t1 - t2) / 1000 - hours * 3600) / 60);
		if (minutes <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 格式化时间
	 * 
	 * @param formatTime
	 * @return
	 */
	public static String getTimeStampNumberFormat(Date formatTime) {
		SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss",
				new Locale("zh", "cn"));
		return m_format.format(formatTime);
	}

	public static String getSimpleDateFormat(String time) throws ParseException {
		String[] hhmm = time.split(":");
		String hh = hhmm[0];
		String mm = hhmm[1];
		int t = Integer.valueOf(hh);
		if (t < 12) {
			t = Integer.valueOf(hh) + 12;
		}
		return t + ":" + mm;
	}

	public static String advisorytime(String advisorydate) {
		String[] hhmm = advisorydate.split(",");
		String[] hh = hhmm[0].split("-");
		return hh[0] + "";
	}

	/**
	 * 判断上午还是下午
	 * 
	 * @param time
	 * @throws ParseException
	 */
	public static int GregorianTest(String time) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = dateFormat.parse(time);
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTime(d);
		return ca.get(GregorianCalendar.AM_PM);
	}

	public static int GregorianTest2(String time) throws ParseException {
		String[] hhmm = time.split(",");
		String[] hh = hhmm[0].split("-");
		if (Integer.valueOf((hh[1] + "").trim().substring(3)) > 12) {
			return 1;
		}
		return 0;
	}

	public static String forenoontime(String advisorytime) {
		String[] hhmm = advisorytime.split(",");
		String[] hh = hhmm[0].split("-");
		return hh[1] + "";
	}

	public static String afternoontime(String advisorytime) {
		String[] hhmm = advisorytime.split(",");
		if (hhmm.length > 1) {
			String[] hh = hhmm[1].split("-");
			return hh[1] + "";
		}
		return null;
	}

	/**
	 * 获取时分
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String presentTime(String time) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar calendar = new GregorianCalendar();// 子类实例化
		calendar.setTime(formatter.parse(time));
		return calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE);
	}

	public static boolean isDateBefore(String date1, String date2)
			throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(formatter.parse(date1));
		Calendar c2 = Calendar.getInstance();
		c2.setTime(formatter.parse(date2));
		System.out.println(c1.before(c2));
		return c1.before(c2);
	}

	public static String noontime(String advisorytime) {
		String[] hhmm = advisorytime.split(",");
		String[] hh = hhmm[1].split("-");
		return hh[0] + "";
	}

	public static String formatting(String time) {
		Calendar now = Calendar.getInstance();
		now.setTime(DateUtil.firstSeconds(time));
		int min = now.get(Calendar.MINUTE);
		if (min > 30) {
			return DateUtil.getPreTime(time, (30 - min) + "");
		} else {
			return DateUtil.getPreTime(time, (60 - min) + "");
		}
	}

	/**
	 * 添加月
	 * 
	 * @param datetime
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public static Date getMonthReduce(String datetime, Integer month)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		Date date = sdf.parse(datetime);
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
		return calendar.getTime();
	}

	/**
	 * 计算时间差值
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	/**
	 * 参数为Date日期比大小,比时分秒
	 * 
	 * @param d1
	 * @param d2
	 * @return(d1>d2 返回1，d1<d2 返回-1,d1=d2 返回0)
	 */
	public static int timeCompare(Date d1, Date d2) {
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		try {
			ca1.setTime(d1);
			ca2.setTime(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ca1.compareTo(ca2);
	}

	/**
	 * GMT时间转换(Mon Feb 13 08:00:00 GMT+08:00 2012)
	 * 
	 * @param lastModified
	 * @return
	 */
	public static String GMTToDate(String lastModified) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
			Date date = sf.parse(lastModified);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String result = sdf.format(date);
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getYesterdayDateStr() {
		String defaultStartDate = null;
		try {
			Date dNow = new Date(); // 当前时间
			System.out.println(dNow);
			Date dBefore = new Date();
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
			dBefore = calendar.getTime(); // 得到前一天的时间

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 设置时间格式
			defaultStartDate = sdf.format(dBefore); // 格式化前一天
			String string = sdf.format(dNow);
			System.out.println(dNow);
			System.out.println(dNow);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultStartDate;
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @return
	 */
	public static Date getYesterdayDate() {
		Date dBefore = null;
		try {
			Date dNow = new Date(); // 当前时间
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
			dBefore = calendar.getTime(); // 得到前一天的时间

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dBefore;
	}

	/**
	 * 获取上月最后的日期
	 * 
	 * @return
	 */
	public static Date getLastDay() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 获取前月的最后一天
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		String lastDay = format.format(cale.getTime());
		return cale.getTime();

	}

	/**
	 * 更新传入的格式格式化时间字符串
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDate(String date, String foramt)
			throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat(foramt);

		Date date2 = format.parse(date);

		return date2;

	}

	/**
	 * 上周的今天
	 * 
	 * @return
	 */
	public static Date getLastWeekThisDay() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, -1);
		return c.getTime();
	}

	/**
	 * 获取上一年的第一天
	 * 
	 * @param args
	 */
	public static Date getFirstDayOfLastYear() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		return c.getTime();
	}

	/**
	 * 获取传入的日期是周几
	 * 
	 * @return 0:周日 1-6:周一到周六
	 */
	public static int getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (c.get(Calendar.DAY_OF_WEEK) - 1);
	}

	public static void main(String[] args) throws ParseException {
		Date date = strToDate("08:00", "HH:mm");
		Date date1 = strToDate("2015-11-19", "yyyy-MM-dd");
		System.out.println(date);
		System.out.println(date1);
	}
}